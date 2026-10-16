package com.example.rickotheque.features.episode.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.rickotheque.features.episode.domain.model.Episode
import com.example.rickotheque.features.episode.ui.EpisodeViewModel
import com.example.rickotheque.features.episode.ui.screen.component.EpisodeItem
import com.example.rickotheque.features.episode.ui.screen.component.SeasonItem
import com.example.rickotheque.features.episode.ui.screen.component.SeasonListSkeleton

/** Episode codes look like "S01E01" — this pulls the season number out of it. */
private val SEASON_REGEX = Regex("""S(\d+)E\d+""")
private val Episode.seasonNumber: Int
    get() = SEASON_REGEX.find(episode)?.groupValues?.get(1)?.toIntOrNull() ?: 0

@Composable
fun MainEpisodesListScreen(viewModel: EpisodeViewModel, navController: NavHostController?) {
    val isLoading by viewModel.isLoadingList.collectAsState()
    val episodes by viewModel.episodes.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        viewModel.fetchAllEpisodes()
    }

    val seasons = remember(episodes) {
        episodes.groupBy { it.seasonNumber }.toSortedMap()
    }

    var expandedSeasons by remember { mutableStateOf(setOf<Int>()) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Gated on `isLoading` alone (not `episodes.isEmpty()`): episodes stream in
        // page by page while this is true, so revealing the list only once it flips
        // to false avoids seasons popping in one at a time.
        if (isLoading) {
            SeasonListSkeleton()
        } else {
            LazyColumn {
                seasons.forEach { (season, episodesInSeason) ->
                    val expanded = season in expandedSeasons

                    item(key = "season-$season") {
                        SeasonItem(
                            season = season,
                            episodeCount = episodesInSeason.size,
                            expanded = expanded,
                            onClick = {
                                expandedSeasons = if (expanded) {
                                    expandedSeasons - season
                                } else {
                                    expandedSeasons + season
                                }
                            },
                        )
                    }

                    if (expanded) {
                        items(episodesInSeason, key = { it.id }) { episode ->
                            EpisodeItem(
                                name = episode.name,
                                episode = episode.episode,
                                onClick = {
                                    navController?.navigate("episodeDetail/${episode.id}")
                                },
                                modifier = Modifier.padding(start = 16.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}
