plugins {
    id("com.android.application").apply(false)
    id("com.android.library").apply(false)
    kotlin("android").apply(false)
    kotlin("multiplatform").apply(false)
    id("org.jetbrains.compose").apply(false)
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

allprojects {
    tasks.withType(org.jetbrains.kotlin.gradle.dsl.KotlinCompile::class.java).configureEach {
        kotlinOptions {

            val path = layout.buildDirectory.dir("compose_metrics").get().asFile.absolutePath

            freeCompilerArgs += listOf(
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=$path",
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=$path",
            )
        }
    }
}