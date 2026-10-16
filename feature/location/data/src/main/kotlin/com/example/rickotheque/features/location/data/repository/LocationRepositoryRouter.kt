package com.example.rickotheque.features.location.data.repository

import com.example.rickotheque.core.datasource.DataSource
import com.example.rickotheque.core.datasource.DataSourceSelector
import com.example.rickotheque.features.location.domain.model.Location
import com.example.rickotheque.features.location.domain.repository.LocationRepository

/**
 * Forwards to the REST (Ktor) or GraphQL (Apollo) implementation picked on
 * the choice screen, read at call time so switching the transport never
 * requires rebuilding the Koin graph. Same pattern as EpisodeRepositoryRouter
 * and CharacterRepositoryRouter — nothing to change here once both
 * implementations are done.
 */
class LocationRepositoryRouter(
    private val dataSourceSelector: DataSourceSelector,
    private val restRepository: LocationRepository,
    private val graphqlRepository: LocationRepository,
) : LocationRepository {
    override suspend fun getLocationById(id: Int): Location? =
        when (dataSourceSelector.current.value) {
            DataSource.REST -> restRepository.getLocationById(id)
            DataSource.GRAPHQL -> graphqlRepository.getLocationById(id)
        }
}
