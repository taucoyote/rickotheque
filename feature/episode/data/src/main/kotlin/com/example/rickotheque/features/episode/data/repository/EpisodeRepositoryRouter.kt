package com.example.rickotheque.features.episode.data.repository

import com.example.rickotheque.core.datasource.DataSource
import com.example.rickotheque.core.datasource.DataSourceSelector
import com.example.rickotheque.features.episode.domain.model.EpisodeResponse
import com.example.rickotheque.features.episode.domain.repository.EpisodeRepository

/**
 * Forwards to the REST (Ktor) or GraphQL (Apollo) implementation picked on
 * the choice screen, read at call time so switching the transport never
 * requires rebuilding the Koin graph.
 */
class EpisodeRepositoryRouter(
    private val dataSourceSelector: DataSourceSelector,
    private val restRepository: EpisodeRepository,
    private val graphqlRepository: EpisodeRepository,
) : EpisodeRepository {
    override suspend fun getEpisodeList(page: Int): EpisodeResponse =
        when (dataSourceSelector.current.value) {
            DataSource.REST -> restRepository.getEpisodeList(page)
            DataSource.GRAPHQL -> graphqlRepository.getEpisodeList(page)
        }
}
