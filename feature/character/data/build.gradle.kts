plugins {
    id("rickotheque.android.data")
}

android {
    namespace = "com.example.rickotheque.features.character.data"
}

dependencies {
    implementation(projects.feature.character.domain)
    implementation(projects.core.network)
    implementation(projects.core.network.graphql)
    implementation(projects.core.datasource)

    implementation(libs.ktor.client.core)
    implementation(libs.gson)
    implementation(libs.apollo.runtime)

    testImplementation(libs.ktor.client.mock)
}
