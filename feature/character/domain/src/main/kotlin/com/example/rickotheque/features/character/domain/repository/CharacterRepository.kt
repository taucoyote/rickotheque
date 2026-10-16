package com.example.rickotheque.features.character.domain.repository

import com.example.rickotheque.features.character.domain.model.Characters

interface CharacterRepository {
    suspend fun getCharacters(characterList: String): List<Characters>
}
