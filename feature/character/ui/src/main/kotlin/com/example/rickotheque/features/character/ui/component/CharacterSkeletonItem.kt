package com.example.rickotheque.features.character.ui.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/** A single greyed-out, pulsing placeholder standing in for a not-yet-loaded [CharacterItem]. */
@Composable
fun CharacterSkeletonItem(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "character-skeleton")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 700, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "character-skeleton-alpha",
    )

    Column(
        modifier = modifier
            .padding(vertical = 8.dp)
            .width(100.dp)
            .wrapContentHeight()
            .alpha(alpha),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .background(Color.Gray, CircleShape)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(60.dp)
                .height(12.dp)
                .background(Color.Gray, RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(40.dp)
                .height(10.dp)
                .background(Color.Gray, RoundedCornerShape(4.dp))
        )
    }
}

/**
 * Mirrors the `characters.chunked(3)` rows used once real data is loaded
 * (see EpisodeDetails / LocationDetails), so swapping skeleton -> real data
 * doesn't shift the layout around.
 */
@Composable
fun CharacterGridSkeleton(modifier: Modifier = Modifier, count: Int = 6) {
    Column(modifier = modifier.fillMaxWidth()) {
        repeat((count + 2) / 3) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                repeat(3) {
                    CharacterSkeletonItem()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CharacterGridSkeletonPreview() {
    CharacterGridSkeleton()
}
