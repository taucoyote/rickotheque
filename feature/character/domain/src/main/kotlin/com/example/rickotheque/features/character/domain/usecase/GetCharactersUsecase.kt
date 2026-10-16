package com.example.rickotheque.features.character.domain.usecase

import com.example.rickotheque.features.character.domain.repository.CharacterRepository

class GetCharactersUsecase(private val repository: CharacterRepository) {
    suspend operator fun invoke(characterListId: String) = repository.getCharacters(characterListId)
}
