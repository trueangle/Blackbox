import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    jvm {
        withJava()
    }
    sourceSets {
        val jvmMain by getting  {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(project(":blackbox"))
                implementation(project(":sample:movie:sharedApp"))
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "io.github.trueangle.blackbox.sample.movie.desktop.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "io.github.trueangle.blackbox.sample.movie.desktop"
            packageVersion = "1.0.0"
        }
    }
}
