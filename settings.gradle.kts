pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

// Generates a type-safe `projects.*` accessor for every module declared
// below (e.g. `projects.feature.episode.domain` instead of
// `project(":feature:episode:domain")` in String form).
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "Rickotheque"
include(":app")
include(":core:datasource")
include(":core:network")
include(":core:network:graphql")
include(":feature:character:domain")
include(":feature:character:data")
include(":feature:character:ui")
include(":feature:episode:domain")
include(":feature:episode:data")
include(":feature:episode:ui")
include(":feature:location:domain")
include(":feature:location:data")
include(":feature:location:ui")
include(":feature:splash:ui")
include(":feature:choice:ui")
