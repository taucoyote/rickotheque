package com.example.rickotheque.buildlogic

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * Generic Android library module (every `:core:*` and `:feature:*:data`/
 * `:feature:*:ui` module) — `com.android.library` + shared Kotlin/Android
 * config. Each module still sets its own `android.namespace`.
 */
class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.library")

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
            }
        }
    }
}
