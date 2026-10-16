plugins {
    id("rickotheque.android.feature")
}

android {
    namespace = "com.example.rickotheque.features.episode.ui"
}

dependencies {
    implementation(projects.feature.episode.domain)
    // Cross-feature UI embed: EpisodeDetailScreen renders CharacterItem and
    // drives it through CharacterViewModel directly (not just its domain) —
    // see EpisodeDetailScreen.kt.
    implementation(projects.feature.character.ui)
    // Gradle `implementation` deps aren't transitive: EpisodeDetails.kt
    // references the `Characters` domain type directly (not just via
    // character:ui's public API), so it needs this on its own classpath too.
    implementation(projects.feature.character.domain)

    implementation(libs.androidx.navigation.compose)
}
