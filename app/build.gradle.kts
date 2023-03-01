import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("plugin.serialization") version "1.8.10"
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.giphyviewer"
    compileSdk = 33

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.example.giphyviewer"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.kotlinSerialization)
    implementation(libs.androidxCore)
    implementation(libs.appCompat)
    implementation(libs.material)
    implementation(libs.constraintLayout)
    implementation(libs.lifecycleLivedata)
    implementation(libs.lifecycleViewModel)

    implementation(libs.ktorClient)
    implementation(libs.ktorClientCIO)
    implementation(libs.ktorNegotiation)
    implementation(libs.ktorClientSerialization)
    implementation(libs.ktorSerializationJson)

    testImplementation(libs.junit)
    androidTestImplementation(libs.testExtJunit)

    androidTestImplementation(libs.espressoCore)

    implementation(libs.navigationComponent)
    implementation(libs.navigationUIComponent)

    implementation(libs.hilt)
    kapt(libs.hiltCompiler)

    implementation(libs.swipeRefreshLayout)
    implementation(libs.glide)
}

kapt {
    correctErrorTypes = true
}