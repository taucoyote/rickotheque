plugins {
    id("rickotheque.android.feature")
}

android {
    namespace = "com.example.rickotheque.features.location.ui"
}

dependencies {
    implementation(projects.feature.location.domain)
    // Cross-feature UI embed: LocationDetailScreen renders CharacterItem and
    // drives it through CharacterViewModel directly — same pattern as
    // EpisodeDetailScreen in feature:episode:ui.
    implementation(projects.feature.character.ui)
    implementation(projects.feature.character.domain)
}
