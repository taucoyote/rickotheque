plugins {
    id("rickotheque.android.feature")
}

android {
    namespace = "com.example.rickotheque.features.character.ui"
}

dependencies {
    implementation(projects.feature.character.domain)

    implementation(libs.coil.compose)
    implementation(libs.coil.network.ktor3)
    implementation(libs.ktor.client.cio)
}
