package com.example.rickotheque.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * For a feature's `data` module, when split into its own Gradle module
 * (rather than a `data/` package inside a single feature module). Android
 * library (needs `android.*` for networking/persistence), depends on the
 * corresponding `:domain` module (declared per-module, not here, since it
 * may also depend on *other* features' `:domain` modules for allow-listed
 * cross-feature composition).
 *
 * This is also where the feature's Koin module lives once split (binding
 * the domain repository interface to this module's impl + registering use
 * cases) — see e.g. `data/di/CharacterModule.kt` — since `data` is the
 * first module in the dependency graph that can see both the domain
 * contracts and their concrete implementation.
 */
class AndroidDataConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("rickotheque.android.library")

            dependencies {
                add("implementation", libs.findLibrary("kotlinx-coroutines-android").get())
                add("implementation", libs.findLibrary("koin-android").get())

                add("testImplementation", libs.findLibrary("junit").get())
                add("testImplementation", libs.findLibrary("kotlinx-coroutines-test").get())
                add("testImplementation", libs.findLibrary("mockk").get())
            }
        }
    }
}
