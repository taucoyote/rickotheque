plugins {
    id("rickotheque.android.library")
}

android {
    namespace = "com.example.rickotheque.core.network"
}

dependencies {
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.koin.android)
}
