plugins {
    id("rickotheque.android.data")
}

android {
    namespace = "com.example.rickotheque.features.episode.data"
}

dependencies {
    implementation(projects.feature.episode.domain)
    implementation(projects.core.network)
    implementation(projects.core.network.graphql)
    implementation(projects.core.datasource)

    implementation(libs.ktor.client.core)
    implementation(libs.gson)
    implementation(libs.apollo.runtime)

    testImplementation(libs.ktor.client.mock)
}
