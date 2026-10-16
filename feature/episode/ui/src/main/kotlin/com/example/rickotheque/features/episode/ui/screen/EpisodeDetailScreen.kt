package com.example.rickotheque.features.episode.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.rickotheque.features.character.domain.model.Characters
import com.example.rickotheque.features.character.ui.CharacterViewModel
import com.example.rickotheque.features.episode.domain.model.Episode
import com.example.rickotheque.features.episode.ui.screen.component.EpisodeDetails

@Composable
fun EpisodeDetailScreen(
    episode: Episode,
    viewModel: CharacterViewModel,
    onCharacterClick: (Characters) -> Unit = {},
) {
    val isLoadingCharacters by viewModel.isLoadingCharacters.collectAsState()
    val characters by viewModel.characters.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchCharacters(episode.characters)
    }

    EpisodeDetails(episode, isLoadingCharacters, characters, onCharacterClick)
}
