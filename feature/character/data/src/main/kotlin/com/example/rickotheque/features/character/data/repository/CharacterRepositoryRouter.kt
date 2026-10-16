package com.example.rickotheque.features.character.data.repository

import com.example.rickotheque.core.datasource.DataSource
import com.example.rickotheque.core.datasource.DataSourceSelector
import com.example.rickotheque.features.character.domain.model.Characters
import com.example.rickotheque.features.character.domain.repository.CharacterRepository

/**
 * Forwards to the REST (Ktor) or GraphQL (Apollo) implementation picked on
 * the choice screen, read at call time so switching the transport never
 * requires rebuilding the Koin graph.
 */
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
