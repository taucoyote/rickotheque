package com.example.rickotheque.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.rickotheque.core.datasource.DataSource
import com.example.rickotheque.core.datasource.DataSourceSelector
import com.example.rickotheque.features.character.ui.CharacterViewModel
import com.example.rickotheque.features.choice.ui.ChoiceScreen
import com.example.rickotheque.features.episode.ui.EpisodeViewModel
import com.example.rickotheque.features.episode.ui.screen.EpisodeDetailScreen
import com.example.rickotheque.features.episode.ui.screen.MainEpisodesListScreen
import com.example.rickotheque.features.location.ui.LocationViewModel
import com.example.rickotheque.features.location.ui.screen.LocationDetailScreen
import com.example.rickotheque.features.splash.ui.SplashScreen
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

/** Routes that belong to the "Episode" section of the app, title-wise. */
private val EPISODE_ROUTES = setOf("main", "episodeDetail/{episodeId}")

/** Routes that have their own top bar (everything but the onboarding flow). */
private val ROUTES_WITHOUT_TOP_BAR = setOf("splash", "choice")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val episodeViewModel: EpisodeViewModel = koinViewModel()
    val characterViewModel: CharacterViewModel = koinViewModel()
    val locationViewModel: LocationViewModel = koinViewModel()
    val dataSourceSelector: DataSourceSelector = koinInject()

    /** The id at the end of a character's `location`/`origin` URL, e.g. ".../location/20" -> 20. */
    fun navigateToLocation(url: String?) {
        val locationId = url?.substringAfterLast("/")?.toIntOrNull()
        if (locationId != null) {
            navController.navigate("locationDetail/$locationId")
        }
    }

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            if (currentRoute != null && currentRoute !in ROUTES_WITHOUT_TOP_BAR) {
                val title = when (currentRoute) {
                    "main" -> "Rick and Morty episode"
                    in EPISODE_ROUTES -> "Episode"
                    else -> "Location"
                }
                TopAppBar(
                    title = { Text(title, color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                    navigationIcon = {
                        if (navController.previousBackStackEntry != null) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Text(text = "←", fontSize = 22.sp, color = MaterialTheme.colorScheme.onPrimary)
                            }
                        }
                    },
                )
            }
        },
    ) { contentPadding ->
        NavHost(
            navController,
            startDestination = "splash",
            modifier = Modifier.padding(contentPadding),
        ) {
            composable("splash") { SplashScreen(navController) }
            composable("choice") {
                ChoiceScreen(
                    onRestSelected = {
                        dataSourceSelector.select(DataSource.REST)
                        navController.navigate("main") { popUpTo("choice") { inclusive = true } }
                    },
                    onGraphqlSelected = {
                        dataSourceSelector.select(DataSource.GRAPHQL)
                        navController.navigate("main") { popUpTo("choice") { inclusive = true } }
                    },
                )
            }
            composable("main") {
                MainEpisodesListScreen(viewModel = episodeViewModel, navController = navController)
            }
            composable("episodeDetail/{episodeId}") { backStackEntry ->
                val episodeId = backStackEntry.arguments?.getString("episodeId")?.toIntOrNull()
                if (episodeId != null) {
                    val episode = episodeViewModel.getEpisodeById(episodeId)
                    if (episode != null) {
                        EpisodeDetailScreen(
                            episode,
                            viewModel = characterViewModel,
                            onCharacterClick = { character ->
                                // A character's current location is more relevant than its origin,
                                // but fall back to origin when location is unknown.
                                navigateToLocation(character.location?.url ?: character.origin?.url)
                            },
                        )
                    }
                }
            }
            composable("locationDetail/{locationId}") { backStackEntry ->
                val locationId = backStackEntry.arguments?.getString("locationId")?.toIntOrNull()
                if (locationId != null) {
                    LocationDetailScreen(
                        locationId = locationId,
                        locationViewModel = locationViewModel,
                        characterViewModel = characterViewModel,
                    )
                }
            }
        }
    }
}
