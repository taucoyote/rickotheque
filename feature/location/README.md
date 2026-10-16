# Exercise — GraphQL integration for the Location feature

## Context

The project exposes two interchangeable transports to talk to the Rick &
Morty API: **REST (Ktor)** and **GraphQL (Apollo)**. Each feature implements
both, behind a common `Repository` and a `*RepositoryRouter` that picks the
implementation to use at runtime based on the `DataSourceSelector` (the
choice made on the `ChoiceScreen` at app launch).

`Character` and `Episode` already have both versions, and for those two
features REST and GraphQL do exactly the same thing (same number of network
calls, just a different transport). **`Location` is different on purpose**:
its REST part is already written and working (`LocationApiService`) — but
your GraphQL implementation is **not** meant to be a 1:1 behavioral copy of
it. This exercise is about showing a real advantage of GraphQL over REST,
not just reimplementing the same calls with a different client.

## What this is for

There's no "browse all locations" screen in this app. The only way to reach
a `Location` is by tapping a character (in the episode detail screen): the
app follows that character's `location`/`origin` link and opens a
**Location detail screen** showing the location's name/type/dimension and
its residents.

### The REST version needs 2 network calls

1. `GET /location/{id}` → the location, with residents known only as
   **URLs** (`".../api/character/8"`, `".../api/character/14"`, ...).
2. `GET /character/{ids}` → resolve those URLs into full profiles (name,
   image, status...) so the UI can display them.

That's not a REST implementation detail you could optimize away — there is
no REST endpoint that returns a location with its residents already
enriched. It's a hard limit of the API's REST shape.

### Your GraphQL version should need only 1

GraphQL can ask for the location **and** full resident profiles in the same
request. Your job is to make that happen for real, and wire the result so
the follow-up character fetch (triggered by `LocationDetailScreen` to
display residents) becomes a **cache hit** instead of a second network
call — see [`CharacterGraphqlCache`](../character/data/src/main/kotlin/com/example/rickotheque/features/character/data/cache/CharacterGraphqlCache.kt),
already wired for you.

## Goal

Implement `LocationGraphqlApiService.getLocationById(id)` in
`feature/location/data/src/main/kotlin/.../network/LocationGraphqlApiService.kt`
so that:

- it returns a `Location?` whose **displayed content** matches the REST
  version (same name/type/dimension, same residents),
- but it does so fetching full resident profiles in the *same* GraphQL
  request, and pushes them into `CharacterGraphqlCache` so
  `CharacterRepository.getCharacters(...)` doesn't need a second network
  call to resolve them.

Nothing else needs to change: the `Repository`, the `Router`, the DI
(`LocationModule`, already wired to inject `CharacterGraphqlCache`), the
`ViewModel`, and the screen (tap a character → `LocationDetailScreen`) are
already in place.

## What already exists (read before you start)

- `feature/location/domain/.../model/Location.kt` — the target model
  (unchanged: `residents` is still a `List<String>` of URLs — only the
  *number of network calls* changes, not the domain shape).
- `feature/location/domain/.../repository/LocationRepository.kt` — the
  contract (a single method: `getLocationById`).
- `feature/location/data/.../network/LocationApiService.kt` — the REST
  version, already working — this is your "2 calls" reference for what
  gets *displayed*, not for how many requests to make.
- `feature/character/data/.../cache/CharacterGraphqlCache.kt` — the shared
  cache. `putAll(characters)` to fill it, already injected into
  `LocationGraphqlApiService`'s constructor.
- `feature/character/data/.../network/CharacterGraphqlApiService.kt` —
  already checks this cache first (`characterCache.getAllOrNull(ids)`)
  before hitting the network. Nothing to change here either — read it to
  see how the cache is consumed.
- `feature/location/ui/.../screen/LocationDetailScreen.kt` — the screen
  reached by tapping a character. This is what exercises your
  implementation end-to-end, and what you'll use to verify the call count.

### A complete example to follow: the Episode / Character features

- `core/network/graphql/src/main/graphql/CharactersByIds.graphql` and
  `feature/character/data/.../network/CharacterGraphqlApiService.kt` — the
  `toDomain()` mapping from a GraphQL character to our domain `Characters`.
  You'll reuse this exact shape for the residents.
- `feature/episode/data/.../network/EpisodeGraphqlApiService.kt` — general
  style reference for mapping a GraphQL response to a domain model and
  handling nulls (`.orEmpty()`, elvis operator, no `!!`).

## Steps

### 1. Write the GraphQL query — with full resident fields

In `core/network/graphql/src/main/graphql/`, create `LocationById.graphql`.
Unlike a naive REST-parity version (which would only ask `residents { id }`),
ask for the same fields as `CharactersByIds.graphql` so you get full
profiles in this one request:

```graphql
query LocationById($id: ID!) {
  location(id: $id) {
    id
    name
    type
    dimension
    created
    residents {
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
}
```

> The full schema is already available in
> `core/network/graphql/src/main/graphql/schema.json` (used by Apollo for
> code generation / autocompletion). You can also explore the schema online
> at https://rickandmortyapi.com/graphql.

### 2. Generate Apollo sources

Sync Gradle (or run the `generateApolloSources` task of the
`core:network:graphql` module). You should get a generated class
`LocationByIdQuery` (and its nested types, including a `Residents` type
shaped just like `CharactersByIdsQuery.CharactersById`).

```
./gradlew :core:network:graphql:generateApolloSources
```

### 3. Implement `LocationGraphqlApiService`

In `feature/location/data/.../network/LocationGraphqlApiService.kt`, replace
the `TODO()` with:

- the call `apolloClient.query(LocationByIdQuery(id = id.toString())).execute()`,
- map `location.residents` to full `Characters` domain objects — copy the
  `toDomain()` mapping from `CharacterGraphqlApiService` (it's the exact
  same generated shape), then call `characterCache.putAll(residentCharacters)`,
- map the response to `Location` itself: `residents` on the *domain* model
  stays a `List<String>` of URLs rebuilt from ids
  (`"https://rickandmortyapi.com/api/character/${it.id}"`), exactly like
  `EpisodeGraphqlApiService` already does for its `characters` field,
- return `null` when the location doesn't exist.

### 4. Verify

- Compile: `./gradlew :feature:location:data:compileDebugKotlin`
- Run the app, go to the choice screen (`ChoiceScreen`), pick **REST**. Open
  an episode, tap a character — the location + its residents should display
  correctly (2 network calls happened, that's expected for REST).
- Restart picking **GraphQL**: tapping the same character must display the
  *same* location and residents — but this time, check with Logcat or a
  network inspector (e.g. Flipper) that only **1** network call was made to
  `rickandmortyapi.com/graphql` for the whole screen, not 2.
- Bonus: add a log line (or a test) proving `CharacterGraphqlApiService`
  took the cache-hit path (`getAllOrNull` returned non-null) instead of
  calling `apolloClient.query(CharactersByIdsQuery(...))`.

## Acceptance criteria

- [ ] `LocationGraphqlApiService.getLocationById` no longer contains a
      `TODO()`.
- [ ] The *displayed* result is identical between REST and GraphQL (same
      location name/type/dimension, same residents) when tapping the same
      character.
- [ ] In GraphQL mode, opening a location detail screen triggers exactly
      **one** network request — not two.
- [ ] The build compiles and existing tests still pass
      (`./gradlew :feature:location:data:test`, `:feature:character:data:test`).
- [ ] The code follows the same style as `CharacterGraphqlApiService` /
      `EpisodeGraphqlApiService` (private `toDomain()` mapping, null-handling
      with `.orEmpty()` / elvis operator rather than `!!`).

## Going further (optional)

- A test that fails if REST and GraphQL diverge on the *displayed* data for
  a fixed id, while still proving the call-count difference.
- Handle the "location unknown" case gracefully in the UI (some characters
  have no `location`/`origin` link at all).
