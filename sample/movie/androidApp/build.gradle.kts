plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "io.github.trueangle.blackbox.sample.movie"
    compileSdk = 34

    defaultConfig {
        applicationId = "io.github.trueangle.blackbox.sample.movie"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.7"
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        jvmToolchain(17)
    }
}

dependencies {
    implementation(project(":blackbox"))
    implementation(project(":sample:movie:sharedApp"))

    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.appcompat)
    implementation(libs.android.material)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.foundation)

    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.windowSizeClass)

    testImplementation(libs.junit)
}