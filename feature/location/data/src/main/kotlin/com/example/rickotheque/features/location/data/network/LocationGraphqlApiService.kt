package com.example.rickotheque.features.location.data.network

import com.apollographql.apollo.ApolloClient
import com.example.rickotheque.features.character.data.cache.CharacterGraphqlCache
import com.example.rickotheque.features.location.domain.model.Location

/**
 * ============================================================
 *  EXERCICE - GraphQL integration
 * ============================================================
 * GraphQL counterpart of [LocationApiService] — but this one is
 * **deliberately NOT a 1:1 behavioral copy of REST**. This is the whole
 * point of the exercise: show a real advantage of GraphQL over REST, not
 * just a different transport for the same calls.
 *
 * REST reference (`LocationApiService` + `LocationDetailScreen`) needs
 * TWO network calls to show a location with its residents:
 *   1. GET /location/{id}        -> location + resident *URLs* only
 *   2. GET /character/{ids}      -> resolve those URLs into full profiles
 * That's a hard limit of the REST API: there's no "location with enriched
 * residents" endpoint.
 *
 * GraphQL can ask for both in the SAME request. Your job: make this
 * implementation do exactly ONE network call, by fetching full resident
 * profiles directly inside the location query, then stashing them in
 * [CharacterGraphqlCache] so `CharacterRepository.getCharacters(...)`
 * (called right after by LocationDetailScreen to display residents) finds
 * them already there and skips its own network call entirely.
 *
 * To do:
 * 1. In `core/network/graphql/src/main/graphql`, create `LocationById.graphql`,
 *    asking for FULL resident fields (not just `id`, unlike a naive REST-parity
 *    version would) — mirror the fields used by `CharactersByIds.graphql`:
 *
 *      query LocationById($id: ID!) {
 *        location(id: $id) {
 *          id
 *          name
 *          type
 *          dimension
 *          created
 *          residents {
 *            id
 *            name
 *            status
 *            species
 *            gender
 *            image
 *            created
 *            origin { id name }
 *            location { id name }
 *            episode { id }
 *          }
 *        }
 *      }
 *
 * 2. Run Apollo's code generation (Gradle sync, or the
 *    `generateApolloSources` task of `core:network:graphql`) to get the
 *    generated `LocationByIdQuery` class.
 * 3. Call `apolloClient.query(LocationByIdQuery(id = id.toString())).execute()`.
 * 4. Map `location.residents` to full `Characters` domain objects (same
 *    `toDomain()` shape as `CharacterGraphqlApiService.CharactersByIdsQuery.CharactersById.toDomain()`
 *    in feature:character:data — copy that mapping, it's the exact same
 *    generated shape) and push them into the cache:
 *    `characterCache.putAll(residentCharacters)`.
 * 5. Map the response to [Location] itself, same as before: `residents`
 *    on the *domain* model stays a `List<String>` of URLs (rebuilt from
 *    ids, `"https://rickandmortyapi.com/api/character/${it.id}"`) — the
 *    domain shape doesn't change, only the number of network calls does.
 *    Return `null` when the location doesn't exist.
 *
 * Verify with Logcat / a network inspector (e.g. Flipper): in REST mode,
 * opening a location detail screen fires 2 requests; in GraphQL mode, 1.
 */
class LocationGraphqlApiService(
    private val apolloClient: ApolloClient,
    private val characterCache: CharacterGraphqlCache,
) {

    suspend fun getLocationById(id: Int): Location? {
        TODO("GraphQL exercise - see the comment above this class")
    }
}
