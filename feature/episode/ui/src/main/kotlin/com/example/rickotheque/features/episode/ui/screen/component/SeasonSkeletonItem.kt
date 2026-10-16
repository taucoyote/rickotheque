package com.example.rickotheque.features.episode.ui.screen.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/** A single greyed-out, pulsing placeholder standing in for a not-yet-loaded [SeasonItem]. */
@Composable
fun SeasonSkeletonItem(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "season-skeleton")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 700, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "season-skeleton-alpha",
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .alpha(alpha),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(20.dp)
                    .background(Color.Gray, RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.25f)
                    .height(14.dp)
                    .background(Color.Gray, RoundedCornerShape(4.dp))
            )
        }
    }
}

/** Shown instead of the season list while episodes are still loading, so nothing pops in one by one. */
@Composable
fun SeasonListSkeleton(modifier: Modifier = Modifier, count: Int = 5) {
    Column(modifier = modifier.fillMaxWidth()) {
        repeat(count) {
            SeasonSkeletonItem()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SeasonListSkeletonPreview() {
    SeasonListSkeleton()
}
