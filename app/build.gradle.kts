import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

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

        val giphyApiKey = gradleLocalProperties(rootDir).getProperty("giphy_api_key") as String
        val url = "https://api.giphy.com/v1/gifs/trending"

        buildConfigField("String", "BASE_URL", "\"$url\"")
        buildConfigField("String", "GIPHY_API_KEY", "\"$giphyApiKey\"")

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
    implementation(libs.mokoPaging)
}

kapt {
    correctErrorTypes = true
}