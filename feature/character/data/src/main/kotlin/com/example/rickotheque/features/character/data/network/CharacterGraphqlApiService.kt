package com.example.rickotheque.features.character.data.network

import com.apollographql.apollo.ApolloClient
import com.example.rickotheque.core.network.graphql.CharactersByIdsQuery
import com.example.rickotheque.features.character.data.cache.CharacterGraphqlCache
import com.example.rickotheque.features.character.domain.model.Characters
import com.example.rickotheque.features.character.domain.model.Location
import com.example.rickotheque.features.character.domain.model.Origin

/** GraphQL counterpart of [CharacterApiService], backed by Apollo instead of Ktor. */
class CharacterGraphqlApiService(
    private val apolloClient: ApolloClient,
    private val characterCache: CharacterGraphqlCache,
) {

    suspend fun getCharacters(characterListIds: String): List<Characters> {
        val ids = characterListIds.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        if (ids.isEmpty()) return emptyList()

        // Cache hit: these characters were already fully resolved by a
        // previous GraphQL call (e.g. LocationGraphqlApiService fetching a
        // location's residents in the same request) — no network call needed.
        // Tradoff for educational purpose, not very recommanded usually ;).
        characterCache.getAllOrNull(ids)?.let { return it }

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
