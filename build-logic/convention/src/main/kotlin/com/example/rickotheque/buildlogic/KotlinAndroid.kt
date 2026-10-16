package com.example.rickotheque.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project

/**
 * Configuration shared by every Android module (application or library):
 * SDK levels and Java source/target compatibility. Mirrors what was
 * previously hand-copied into `app/build.gradle.kts`.
 *
 * `CommonExtension` only exposes plain properties (no block-lambda DSL
 * overloads) — those live on the concrete `ApplicationExtension`/
 * `LibraryExtension` interfaces instead — so this configures things via
 * direct property assignment.
 *
 * Since AGP 9.0, Kotlin support for Android modules is built in — applying
 * `org.jetbrains.kotlin.android` ourselves is no longer necessary (and is
 * in fact rejected by AGP). The Kotlin compiler's `jvmTarget` then defaults
 * to `compileOptions.targetCompatibility` below, so it doesn't need to be
 * configured separately either.
 */
internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension,
) {
    commonExtension.compileSdk = 37
    commonExtension.defaultConfig.minSdk = 28
    commonExtension.compileOptions.sourceCompatibility = JavaVersion.VERSION_21
    commonExtension.compileOptions.targetCompatibility = JavaVersion.VERSION_21
}
