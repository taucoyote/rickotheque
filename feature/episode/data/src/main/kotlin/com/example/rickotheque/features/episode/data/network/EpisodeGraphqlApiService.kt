package com.example.rickotheque.features.episode.data.network

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.example.rickotheque.core.network.graphql.EpisodesQuery
import com.example.rickotheque.features.episode.domain.model.Episode
import com.example.rickotheque.features.episode.domain.model.EpisodeResponse
import com.example.rickotheque.features.episode.domain.model.Info

/** GraphQL counterpart of [EpisodeApiService], backed by Apollo instead of Ktor. */
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
