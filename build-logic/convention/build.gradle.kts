plugins {
    `kotlin-dsl`
}

group = "com.example.rickotheque.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}

dependencies {
    compileOnly(plugin(libs.plugins.android.application))
    compileOnly(plugin(libs.plugins.android.library))
    compileOnly(plugin(libs.plugins.jetbrains.kotlin.android))
    compileOnly(plugin(libs.plugins.jetbrains.kotlin.jvm))
    compileOnly(plugin(libs.plugins.jetbrains.kotlin.compose))
}

// Turns a Gradle plugin alias from the version catalog into a regular
// dependency notation (`pluginId:pluginId.gradle.plugin:version`), so its
// jar ends up on this project's compile classpath and its version can be
// applied programmatically (`pluginManager.apply(id)`) from convention
// plugin classes below, without redeclaring versions here.
fun DependencyHandlerScope.plugin(plugin: Provider<PluginDependency>) =
    plugin.map { "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}" }

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "rickotheque.android.application"
            implementationClass = "com.example.rickotheque.buildlogic.AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "rickotheque.android.application.compose"
            implementationClass = "com.example.rickotheque.buildlogic.AndroidApplicationComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = "rickotheque.android.library"
            implementationClass = "com.example.rickotheque.buildlogic.AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "rickotheque.android.library.compose"
            implementationClass = "com.example.rickotheque.buildlogic.AndroidLibraryComposeConventionPlugin"
        }
        register("androidFeature") {
            id = "rickotheque.android.feature"
            implementationClass = "com.example.rickotheque.buildlogic.AndroidFeatureConventionPlugin"
        }
        register("androidData") {
            id = "rickotheque.android.data"
            implementationClass = "com.example.rickotheque.buildlogic.AndroidDataConventionPlugin"
        }
        register("domain") {
            id = "rickotheque.domain"
            implementationClass = "com.example.rickotheque.buildlogic.DomainConventionPlugin"
        }
    }
}
