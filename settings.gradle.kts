pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }

    plugins {
        val kotlinVersion = "1.9.0"
        val agpVersion = "8.1.1"
        val composeVersion = "1.5.10-beta02"
        val serialization = "1.9.0"

        kotlin("jvm").version(kotlinVersion)
        kotlin("multiplatform").version(kotlinVersion)
        kotlin("android").version(kotlinVersion)
        kotlin("plugin.serialization").version(serialization)

        id("com.android.application").version(agpVersion)
        id("com.android.library").version(agpVersion)
        id("com.android.application").version(agpVersion)

        id("org.jetbrains.compose").version(composeVersion)
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
    }
}

rootProject.name = "Blackbox"
include(":blackbox")
include(":blackbox-android")
include(":sample:movie:androidApp")
include(":sample:movie:core")
include(":sample:movie:auth")
include(":sample:movie:design")
include(":sample:movie:ticketing")
include(":sample:movie:sharedApp")
