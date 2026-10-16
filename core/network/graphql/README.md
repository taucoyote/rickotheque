# GraphQL in this project — how it works

This is a walkthrough of the GraphQL integration that's **already implemented**
(Episode + Character features) It does not cover the Location feature's GraphQL exercise —
see `feature/location/README.md` for that.

## 1. The big picture: GraphQL vs REST, in this codebase

With REST, you hit a fixed URL per resource and get back whatever shape the
server decides (`GET /api/episode` always returns the full episode object).

With GraphQL, there's **one single endpoint**
(`https://rickandmortyapi.com/graphql`), and the client sends a **query** —
basically "here's the shape of data I want" — and the server returns
*exactly* that shape, no more, no less. You ask for fields by name,
including fields on nested/related objects, in one round trip.

In this project, each feature has a REST implementation (Ktor) and a
GraphQL implementation (Apollo) of the *same* `Repository` interface, so the
rest of the app (ViewModel, UI) doesn't care which one is actually running.

## 2. Where GraphQL lives in the project

```
core/network/graphql/                     <- shared GraphQL plumbing
  build.gradle.kts                        <- Apollo Gradle plugin config
  src/main/graphql/
    schema.json                           <- the full API schema (downloaded once)
    Episodes.graphql                      <- a query 
    CharactersByIds.graphql               <- another query
  src/main/kotlin/.../di/GraphqlNetworkModule.kt  <- the ApolloClient singleton

feature/episode/data/.../network/EpisodeGraphqlApiService.kt
feature/character/data/.../network/CharacterGraphqlApiService.kt
```

The `.graphql` files are **not executed directly**. They're input to a
**code generator** (Apollo), configured here:

```kotlin
// core/network/graphql/build.gradle.kts
plugins {
    id("rickotheque.android.library")
    alias(libs.plugins.apollo)
}

apollo {
    service("service") {
        packageName.set("com.example.rickotheque.core.network.graphql")
    }
}
```

At build time, Apollo reads `schema.json` + every `.graphql` file in
`src/main/graphql`, and generates **typed Kotlin classes** for each query —
one class per query, with nested classes matching the shape you asked for.
That's the big difference with REST/Gson: you don't hand-write a data class
and hope the JSON matches; the generated class *is* guaranteed to match what
the server can return for that exact query.

## 3. The ApolloClient — one shared "connection" to the API

```kotlin
// core/network/graphql/.../di/GraphqlNetworkModule.kt
private const val RICK_AND_MORTY_GRAPHQL_ENDPOINT = "https://rickandmortyapi.com/graphql"

val coreNetworkGraphqlModule =
    module {
        single {
            ApolloClient.Builder()
                .serverUrl(RICK_AND_MORTY_GRAPHQL_ENDPOINT)
                .build()
        }
    }
```

This is the GraphQL equivalent of the Ktor `HttpClient` singleton used for
REST. One `ApolloClient`, injected via Koin into every feature's
`*GraphqlApiService`.

## 4. Writing a query — the `Episodes` example

```graphql
# Episodes.graphql
query Episodes($page: Int) {
  episodes(page: $page) {
    info {
      count
      pages
      next
      prev
    }
    results {
      id
      name
      air_date
      episode
      created
      characters {
        id
      }
    }
  }
}
```

Reading this line by line:

- `query Episodes($page: Int)` — names the query `Episodes` (this name
  becomes the generated class name: `EpisodesQuery`) and declares one
  **variable**, `$page`, of type `Int` (nullable, since there's no `!`).
- `episodes(page: $page)` — calls the `episodes` field on the schema's root
  `Query` type, passing our variable as its `page` argument. This mirrors
  the REST `?page=` query param, just expressed as a typed argument instead
  of a raw string in a URL.
- Everything inside `{ ... }` is the **selection set**: the exact fields we
  want back. `info { count pages next prev }` mirrors the REST pagination
  `info` object. `results { id name air_date episode created characters { id } }`
  — notice `characters { id }`: we only ask for the `id` of each related
  character, not the full character object, because that's all we need here
  (the character's own data is fetched separately, by id, through the
  Character feature).

This selectivity is the core GraphQL idea: **you only pay (in payload size)
for what you ask for**, and nested/related data can be included in the same
request without a dedicated endpoint for it.

Running Apollo's code generation against this file produces a Kotlin class
roughly shaped like:

```kotlin
class EpisodesQuery(val page: Optional<Int?>) : Query<EpisodesQuery.Data> {
    data class Data(val episodes: Episodes?)
    data class Episodes(val info: Info?, val results: List<Result?>?)
    data class Info(val count: Int?, val pages: Int?, val next: Any?, val prev: Any?)
    data class Result(val id: String?, val name: String?, val air_date: String?, ...)
    ...
}
```

(simplified — the real generated code is in a build-generated folder, not
something you edit by hand)

Note everything is **nullable** (`String?`, `Int?`, lists of nullable items
`List<Result?>?`). That's because GraphQL fields can always come back `null`
(missing data, partial errors, etc.), so Apollo's generated types reflect
that honestly — this is different from the REST+Gson models in the project,
which assume the JSON always matches the Kotlin shape.

## 5. Using the generated class + mapping to our domain model

```kotlin
// feature/episode/data/.../network/EpisodeGraphqlApiService.kt
class EpisodeGraphqlApiService(private val apolloClient: ApolloClient) {

    suspend fun getEpisodeList(page: Int): EpisodeResponse {
        val response = apolloClient.query(EpisodesQuery(page = Optional.present(page))).execute()
        val episodes = response.data?.episodes ?: return EpisodeResponse(Info(0, 0, null, null), emptyList())

        val info = episodes.info
        return EpisodeResponse(
            info = Info(
                count = info?.count ?: 0,
                pages = info?.pages ?: 0,
                next = info?.next?.toString(),
                prev = info?.prev?.toString(),
            ),
            results = episodes.results.orEmpty().filterNotNull().map { it.toDomain() },
        )
    }

    private fun EpisodesQuery.Result.toDomain() =
        Episode(
            id = id?.toIntOrNull() ?: 0,
            name = name.orEmpty(),
            air_date = air_date.orEmpty(),
            episode = episode.orEmpty(),
            characters = characters.filterNotNull().map { "https://rickandmortyapi.com/api/character/${it.id}" },
            url = "https://rickandmortyapi.com/api/episode/${id.orEmpty()}",
            created = created.orEmpty(),
        )
}
```

Key things happening here:

1. **`apolloClient.query(EpisodesQuery(...)).execute()`** — build the query
   object (with its variables) and send it over the network. `execute()` is
   a suspend function, so this is called from a coroutine, same as the Ktor
   REST calls.
2. **`Optional.present(page)`** — Apollo's way of distinguishing "I'm
   explicitly passing this value" from "I'm omitting this variable
   entirely" (`Optional.Absent`). This matters in GraphQL because a
   variable that's *absent* vs. explicitly `null` can behave differently
   server-side (e.g. "don't filter on this field" vs. "filter for null").
3. **`response.data?.episodes`** — the response wraps everything in `.data`
   (there's also a `.errors` list for partial GraphQL errors, not used here
   for simplicity).
4. **`toDomain()`** — this is the important architectural point: the
   GraphQL-generated type (`EpisodesQuery.Result`) is **private to this
   file**. It never leaks outside `EpisodeGraphqlApiService`. Everything
   gets mapped into the same `Episode` domain model that the REST
   implementation (`EpisodeApiService`) also produces. That's what makes
   the `EpisodeRepositoryRouter` able to swap REST ↔ GraphQL with zero code
   changes anywhere else in the app — both implementations speak the same
   domain language.
5. Note how `characters.filterNotNull().map { "https://rickandmortyapi.com/api/character/${it.id}" }`
   **reconstructs a REST-style URL from a GraphQL id**. GraphQL only gave us
   `characters { id }`; the domain model expects a list of character URLs
   (because that's what REST naturally returns), so the GraphQL side fakes
   that shape to stay compatible.

## 6. A second example: fetching several characters by id

```graphql
# CharactersByIds.graphql
query CharactersByIds($ids: [ID!]!) {
  charactersByIds(ids: $ids) {
    id
    name
    status
    species
    gender
    image
    created
    origin {
      id
      name
    }
    location {
      id
      name
    }
    episode {
      id
    }
  }
}
```

New things here compared to `Episodes.graphql`:

- `$ids: [ID!]!` — a **non-nullable list of non-nullable IDs**. The two `!`
  matter: the outer one means "you must pass a list, not null"; the inner
  one means "no element of that list can be null". This is how GraphQL
  encodes "this is required" directly in the type system, instead of
  runtime validation.
- `origin { id name }` and `location { id name }` — these are **nested
  objects**, not scalar fields. GraphQL lets you select fields several
  levels deep in a single query/round trip — this is the REST equivalent of
  needing to separately call `/location/{id}` for each character's origin
  and location, which REST doesn't do automatically.

And the mapping side:

```kotlin
// feature/character/data/.../network/CharacterGraphqlApiService.kt
class CharacterGraphqlApiService(private val apolloClient: ApolloClient) {

    suspend fun getCharacters(characterListIds: String): List<Characters> {
        val ids = characterListIds.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        if (ids.isEmpty()) return emptyList()

        val response = apolloClient.query(CharactersByIdsQuery(ids)).execute()
        val characters = response.data?.charactersByIds ?: return emptyList()

        return characters.filterNotNull().map { it.toDomain() }
    }

    private fun CharactersByIdsQuery.CharactersById.toDomain() =
        Characters(
            id = id?.toIntOrNull() ?: 0,
            name = name.orEmpty(),
            status = status.orEmpty(),
            species = species.orEmpty(),
            gender = gender,
            origin = origin?.let { Origin(name = it.name.orEmpty(), url = it.id.toLocationUrl()) },
            location = location?.let { Location(name = it.name.orEmpty(), url = it.id.toLocationUrl()) },
            image = image,
            episode = episode.filterNotNull().map { "https://rickandmortyapi.com/api/episode/${it.id}" },
            created = created,
        )

    private fun String?.toLocationUrl(): String =
        this?.let { "https://rickandmortyapi.com/api/location/$it" }.orEmpty()
}
```

- The method signature (`getCharacters(characterListIds: String)`) takes a
  **comma-separated string of ids**, the exact same input shape as the REST
  version (`CharacterApiService`, which hits `/character/1,2,3`). The
  caller (`CharacterRepository`) doesn't know or care that one
  implementation turns that string into a GraphQL `[ID!]!` list and the
  other bakes it straight into a URL path.
- `origin?.let { Origin(name = ..., url = it.id.toLocationUrl()) }` — same
  idea as before: GraphQL gives us a `location`/`origin` **id**, and we
  rebuild the REST-shaped URL (`.../api/location/{id}`) so the domain model
  stays identical regardless of transport.

## 7. How REST vs GraphQL actually gets chosen at runtime

None of the two `*GraphqlApiService`/`*ApiService` classes are used directly
by the UI. They're wrapped by `Repository` implementations, and a `Router`
picks between them:

```kotlin
// feature/character/data/.../repository/CharacterGraphqlRepositoryImpl.kt
class CharacterGraphqlRepositoryImpl(
    private val characterGraphqlApiService: CharacterGraphqlApiService,
) : CharacterRepository {
    override suspend fun getCharacters(characterList: String): List<Characters> =
        characterGraphqlApiService.getCharacters(characterList)
}

// feature/character/data/.../repository/CharacterRepositoryRouter.kt
class CharacterRepositoryRouter(
    private val dataSourceSelector: DataSourceSelector,
    private val restRepository: CharacterRepository,
    private val graphqlRepository: CharacterRepository,
) : CharacterRepository {
    override suspend fun getCharacters(characterList: String): List<Characters> =
        when (dataSourceSelector.current.value) {
            DataSource.REST -> restRepository.getCharacters(characterList)
            DataSource.GRAPHQL -> graphqlRepository.getCharacters(characterList)
        }
}
```

`DataSourceSelector` just holds whatever the user picked on the
`ChoiceScreen` at app launch. Everything above this layer (use cases,
ViewModels, Compose screens) only ever talks to
`CharacterRepository`/`EpisodeRepository` — it has zero knowledge of Ktor or
Apollo.

## TL;DR to explain to a GraphQL newcomer

1. One endpoint, many possible shapes of data — you describe the shape you
   want in a `.graphql` query file.
2. Apollo's Gradle plugin reads that query + the API's `schema.json` and
   **generates Kotlin classes at build time** matching exactly that shape —
   strongly typed, no manual JSON parsing.
3. You call `apolloClient.query(GeneratedQuery(...)).execute()`, read
   `.data`, and map the generated (very nullable, very GraphQL-shaped) types
   into this project's own domain models, so the rest of the app never sees
   anything GraphQL-specific.
4. Variables (`$page`, `$ids`) are how you parameterize a query, with
   explicit nullability (`Int`, `[ID!]!`) baked into the query itself.
5. Nested selections (`origin { id name }`) let you fetch related data in
   one round trip — something REST would need a second request for.
