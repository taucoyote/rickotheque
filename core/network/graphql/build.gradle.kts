plugins {
    id("rickotheque.android.library")
    alias(libs.plugins.apollo)
}

android {
    namespace = "com.example.rickotheque.core.network.graphql"
}

apollo {
    service("service") {
        packageName.set("com.example.rickotheque.core.network.graphql")
    }
}

dependencies {
    implementation(libs.apollo.runtime)
    implementation(libs.koin.android)
}
