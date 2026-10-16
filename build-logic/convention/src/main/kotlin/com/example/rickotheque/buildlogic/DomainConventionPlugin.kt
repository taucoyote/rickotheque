package com.example.rickotheque.buildlogic

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * For a feature's `domain` module, when split into its own Gradle module
 * (rather than a `domain/` package inside a single feature module). Pure
 * Kotlin JVM, zero Android dependency by construction (no
 * `com.android.library` applied at all, so `android.*`/`androidx.*` simply
 * aren't on the classpath). Only `kotlinx-coroutines-core` (the plain JVM
 * artifact, not `-android`) is added, since domain interfaces expose
 * `Flow`/`suspend fun`.
 *
 * Depend on other features' `:domain` modules only (never `:data`/`:ui`).
 */
class DomainConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.jvm")

            extensions.configure<JavaPluginExtension> {
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
            }

            tasks.withType(KotlinCompile::class.java).configureEach {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_1_8)
                }
            }

            dependencies {
                add("implementation", libs.findLibrary("kotlinx-coroutines-core").get())
                add("testImplementation", libs.findLibrary("junit").get())
                add("testImplementation", libs.findLibrary("kotlinx-coroutines-test").get())
                add("testImplementation", libs.findLibrary("mockk").get())
            }
        }
    }
}
