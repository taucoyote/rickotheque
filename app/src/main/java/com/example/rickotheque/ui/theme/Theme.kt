package com.example.rickotheque.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Rick and Morty's signature "portal" green — the same color already used
 * for the top bar and the season/type cards, so theming it here keeps
 * default-colored Material components (loaders, buttons...) consistent
 * instead of falling back to Material's default purple/blue.
 *
 * Material 3 has no "primaryVariant" concept anymore; `primaryContainer` is
 * used here to stand in for the darker green used in gradients/badges.
 */
val PortalGreen = Color(0xFF00FF00)
val PortalGreenDark = Color(0xFF00B300)
val PortalAccentYellow = Color(0xFFFFEB3B)

private val RickAndMortyColorScheme = lightColorScheme(
    primary = PortalGreen,
    onPrimary = Color.White,
    primaryContainer = PortalGreenDark,
    onPrimaryContainer = Color.White,
    secondary = PortalAccentYellow,
    onSecondary = Color.Black,
)

@Composable
fun RickAndMortyTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = RickAndMortyColorScheme,
        content = content,
    )
}
