package com.example.rickotheque.features.location.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.rickotheque.features.character.ui.CharacterViewModel
import com.example.rickotheque.features.location.ui.LocationViewModel
import com.example.rickotheque.features.location.ui.screen.component.LocationDetails

/**
 * Reached by tapping a character (its `location`/`origin` link). Resolves
 * the location itself plus its residents, exactly like EpisodeDetailScreen
 * does for an episode's characters.
 */
@Composable
fun LocationDetailScreen(
    locationId: Int,
    locationViewModel: LocationViewModel,
    characterViewModel: CharacterViewModel,
) {
    val isLoadingLocation by locationViewModel.isLoadingSelectedLocation.collectAsState()
    val location by locationViewModel.selectedLocation.collectAsState()

    val isLoadingCharacters by characterViewModel.isLoadingCharacters.collectAsState()
    val characters by characterViewModel.characters.collectAsState()

    LaunchedEffect(locationId) {
        locationViewModel.fetchLocationById(locationId)
    }

    LaunchedEffect(location) {
        // Suspends until the fetch is fully resolved (success, failure, or
        // empty residents) — isLoadingCharacters stays true for the whole
        // duration with no gap, so LocationDetails gets a reliable signal.
        location?.let { characterViewModel.fetchCharactersAndAwait(it.residents) }
    }

    LocationDetails(
        isLoadingLocation = isLoadingLocation,
        location = location,
        isLoadingCharacters = isLoadingCharacters,
        characters = characters,
    )
}
