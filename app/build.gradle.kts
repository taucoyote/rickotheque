plugins {
    id("rickotheque.android.application")
    id("rickotheque.android.application.compose")
}

android {
    namespace = "com.example.rickotheque"
}

dependencies {
    implementation(projects.core.network)
    implementation(projects.core.network.graphql)
    implementation(projects.core.datasource)
    implementation(projects.feature.character.data)
    implementation(projects.feature.character.domain)
    implementation(projects.feature.character.ui)
    implementation(projects.feature.episode.domain)
    implementation(projects.feature.episode.data)
    implementation(projects.feature.episode.ui)
    implementation(projects.feature.location.domain)
    implementation(projects.feature.location.data)
    implementation(projects.feature.location.ui)
    implementation(projects.feature.splash.ui)
    implementation(projects.feature.choice.ui)

    implementation(libs.material)

    // Jetpack Compose
    implementation(libs.androidx.navigation.compose)

    // Lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
