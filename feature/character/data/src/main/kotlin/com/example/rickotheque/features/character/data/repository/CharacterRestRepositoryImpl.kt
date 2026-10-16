package com.example.rickotheque.features.character.data.repository

import com.example.rickotheque.features.character.data.network.CharacterApiService
import com.example.rickotheque.features.character.domain.model.Characters
import com.example.rickotheque.features.character.domain.repository.CharacterRepository

class CharacterRestRepositoryImpl(private val characterApiService: CharacterApiService) : CharacterRepository {
    override suspend fun getCharacters(characterList: String): List<Characters> {
        return characterApiService.getCharacters(characterList)
    }
}
