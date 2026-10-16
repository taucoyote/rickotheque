package com.example.rickotheque.features.character.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickotheque.features.character.domain.model.Characters
import com.example.rickotheque.features.character.domain.usecase.GetCharactersUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CharacterViewModel(
    private val getCharactersUsecase: GetCharactersUsecase
) : ViewModel() {
    private var _isLoadingCharacters = MutableStateFlow<Boolean>(false)
    val isLoadingCharacters: StateFlow<Boolean> = _isLoadingCharacters

    private val _characters = MutableStateFlow<List<Characters>>(emptyList())
    val characters: StateFlow<List<Characters>> get() = _characters

    /** Fire-and-forget entry point, used by EpisodeDetailScreen. */
    fun fetchCharacters(characterUrls: List<String>) {
        viewModelScope.launch {
            fetchCharactersAndAwait(characterUrls)
        }
    }

    /**
     * Suspends until the fetch (or reset, for an empty list) is fully done,
     * with `isLoadingCharacters` true for its entire duration and no gap —
     * used by LocationDetailScreen, which needs a reliable "done" signal to
     * know when to reveal its skeleton, rather than inferring it from
     * whether `characters` ended up empty (which is also true on error, or
     * legitimately when there are zero residents).
     */
    suspend fun fetchCharactersAndAwait(characterUrls: List<String>) {
        _isLoadingCharacters.value = true
        try {
            _characters.value = if (characterUrls.isEmpty()) {
                emptyList()
            } else {
                val joinedIds = characterUrls.joinToString(",") { it.substringAfterLast("/") }
                getCharactersUsecase(joinedIds)
            }
        } finally {
            _isLoadingCharacters.value = false
        }
    }
}
