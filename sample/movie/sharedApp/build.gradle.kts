import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    androidTarget()
    jvm("desktop")

    val xcf = XCFramework()
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "sharedApp"
            isStatic = true
            xcf.add(this)
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                implementation(project(":blackbox"))
                implementation(project(":sample:movie:core"))
                implementation(project(":sample:movie:design"))
                implementation(project(":sample:movie:auth"))
                implementation(project(":sample:movie:ticketing"))

                implementation(libs.kamel)
                implementation(libs.jetbrains.ktor)
                implementation(libs.jetbrains.ktor.client.contentNegotiation)
                implementation(libs.jetbrains.ktor.client.loggging)
                implementation(libs.jetbrains.ktor.serializationKotlinxJson)

                implementation(libs.kmpalette.core)
                implementation(libs.kmpalette.extensionsNetwork)

                api(libs.jetbrains.immutableCollections)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.jetbrains.ktor.client.okhttp)
                implementation(compose.uiTooling)
                implementation(compose.preview)
            }
        }

        val iosMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.jetbrains.ktor.client.darwin)
            }
        }
        val iosX64Main by getting {
            dependsOn(iosMain)
        }
        val iosArm64Main by getting {
            dependsOn(iosMain)
        }
        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.common)
                implementation(libs.jetbrains.ktor.client.java)
            }
        }
    }
}

android {
    namespace = "io.github.trueangle.blackbox.sample.movie.shared"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        jvmToolchain(17)
    }
}