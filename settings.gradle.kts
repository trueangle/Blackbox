pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
    }

    plugins {
        val kotlinVersion = "1.9.22"
        val agpVersion = "8.1.1"
        val composeVersion = "1.6.0-beta02"
        val serialization = "1.9.10"

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
includeBuild("convention-plugins")
include(":blackbox")
include(":blackbox-android")
include(":sample:movie:core")
include(":sample:movie:auth")
include(":sample:movie:design")
include(":sample:movie:ticketing")
include(":sample:movie:sharedApp")
include(":sample:movie:desktopApp")
