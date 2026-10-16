package com.example.rickotheque.features.episode.ui.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rickotheque.features.character.domain.model.Characters
import com.example.rickotheque.features.character.ui.component.CharacterGridSkeleton
import com.example.rickotheque.features.character.ui.component.CharacterItem
import com.example.rickotheque.features.episode.domain.model.Episode

/** A small rounded, colored pill used for the episode code / air date. */
@Composable
private fun InfoChip(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f), RoundedCornerShape(50)),
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primaryContainer,
        )
    }
}

@Composable
fun EpisodeDetails(
    episode: Episode,
    isLoadingCharacters: Boolean,
    characters: List<Characters>? = null,
    onCharacterClick: (Characters) -> Unit = {},
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    ) {

        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = episode.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                InfoChip(text = episode.episode)
                InfoChip(text = episode.air_date)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Characters",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            ) {
                if (isLoadingCharacters) {
                    CharacterGridSkeleton(modifier = Modifier.padding(8.dp))
                } else {
                    LazyColumn {
                        items(characters?.chunked(3) ?: emptyList()) { characterGroup ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                characterGroup.forEach { character ->
                                    CharacterItem(character, onClick = { onCharacterClick(character) })
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun EpisodeDetailContentPreview() {
    val mockEpisode = Episode(
        id = 1,
        name = "Pilot",
        air_date = "December 2, 2013",
        episode = "S01E01",
        characters = listOf(
            "https://rickandmortyapi.com/api/character/1",
            "https://rickandmortyapi.com/api/character/2",
            "https://rickandmortyapi.com/api/character/3"
        ),
        url = "https://rickandmortyapi.com/api/episode/1",
        created = "2017-11-10T12:56:33.798Z"
    )
    EpisodeDetails(
        episode = mockEpisode,
        isLoadingCharacters = false,
        characters = null
    )
}
