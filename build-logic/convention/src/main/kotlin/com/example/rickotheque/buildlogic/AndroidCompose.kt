package com.example.rickotheque.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Shared by `rickotheque.android.application.compose` and
 * `rickotheque.android.library.compose`: turns on the Compose build feature
 * and wires the handful of Compose artifacts every UI-bearing module in
 * this repo needs.
 *
 * Kotlin is 2.0+, so the Compose compiler is a standalone Gradle plugin
 * (`org.jetbrains.kotlin.plugin.compose`) applied here instead of being
 * configured via the now-removed `composeOptions.kotlinCompilerExtensionVersion`.
 */
internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension,
) {
    val libs = libs

    pluginManager.apply(libs.findPlugin("jetbrains-kotlin-compose").get().get().pluginId)

    commonExtension.buildFeatures.compose = true

    dependencies {
        add("implementation", libs.findLibrary("androidx-ui").get())
        add("implementation", libs.findLibrary("androidx-material").get())
        add("implementation", libs.findLibrary("androidx-material3-android").get())
        add("implementation", libs.findLibrary("androidx-ui-tooling-preview").get())
        add("debugImplementation", libs.findLibrary("androidx-ui-tooling").get())
    }
}
