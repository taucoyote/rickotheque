package com.example.rickotheque.features.character.data.repository

import com.example.rickotheque.features.character.data.network.CharacterGraphqlApiService
import com.example.rickotheque.features.character.domain.model.Characters
import com.example.rickotheque.features.character.domain.repository.CharacterRepository

class CharacterGraphqlRepositoryImpl(
    private val characterGraphqlApiService: CharacterGraphqlApiService,
) : CharacterRepository {
    override suspend fun getCharacters(characterList: String): List<Characters> {
        return characterGraphqlApiService.getCharacters(characterList)
    }
}
