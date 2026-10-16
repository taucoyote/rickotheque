plugins {
    id("rickotheque.android.library")
    id("rickotheque.android.library.compose")
}

android {
    namespace = "com.example.rickotheque.features.splash.ui"
}

dependencies {
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.coroutines.android)
}
