package com.example.rickotheque.features.character.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.example.rickotheque.features.character.domain.model.Characters

/** Green/red/grey ring around the avatar, echoing the character's status. */
private fun statusColor(status: String): Color = when (status.lowercase()) {
    "alive" -> Color(0xFF4CAF50)
    "dead" -> Color(0xFFE53935)
    else -> Color.Gray
}

@Composable
fun CharacterItem(character: Characters, onClick: () -> Unit = {}) {
    character.let {
        Column(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .width(100.dp)
                .wrapContentHeight()
                .clickable(onClick = onClick)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                val painter = rememberAsyncImagePainter(it.image)
                Image(
                    painter = painter,
                    contentDescription = it.name,
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .border(2.dp, statusColor(it.status), CircleShape)
                )
            }
            Text(
                text = it.name,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 2,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 4.dp)
            )
            Text(
                text = it.species,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CharacterItemPreview() {
    val sampleCharacter = Characters(
        id = 1,
        name = "Rick Sanchez",
        status = "Alive",
        species = "Human",
        image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
    )
    CharacterItem(character = sampleCharacter)
}
