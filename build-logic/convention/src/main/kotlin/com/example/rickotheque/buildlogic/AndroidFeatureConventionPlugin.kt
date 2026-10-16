package com.example.rickotheque.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * For every `:feature:x:ui` module that has a `ViewModel` — applies
 * `android.library` + `android.library.compose` + Koin (+ Compose
 * integration) + lifecycle-viewmodel/runtime-compose, matching what every
 * feature's `build.gradle.kts` currently repeats by hand.
 *
 * UI-only features with no `ViewModel` (e.g. a stateless splash screen)
 * should use `rickotheque.android.library` + `rickotheque.android.library.compose`
 * directly instead, since they don't need the DI/presentation extras this
 * plugin adds.
 */
class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("rickotheque.android.library")
            pluginManager.apply("rickotheque.android.library.compose")

            val libs = libs

            dependencies {
                add("implementation", libs.findLibrary("koin-android").get())
                add("implementation", libs.findLibrary("koin-androidx-compose").get())
                add("implementation", libs.findLibrary("androidx-lifecycle-viewmodel-compose").get())
                add("implementation", libs.findLibrary("androidx-lifecycle-runtime-ktx").get())

                add("testImplementation", libs.findLibrary("junit").get())
                add("testImplementation", libs.findLibrary("kotlinx-coroutines-test").get())
                add("testImplementation", libs.findLibrary("mockk").get())
                add("testImplementation", libs.findLibrary("koin-test").get())
            }
        }
    }
}
