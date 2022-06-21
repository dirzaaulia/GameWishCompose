import com.google.protobuf.gradle.*

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("com.google.protobuf")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    signingConfigs {
        getByName("debug") {
            storeFile = file("D:\\Android Studio\\keystore.jks")
            storePassword = AppConfig.KeyStore.password
            keyAlias = AppConfig.KeyStore.alias
            keyPassword = AppConfig.KeyStore.password
        }
        create("release") {
            storeFile = file("D:\\Android Studio\\keystore.jks")
            storePassword = AppConfig.KeyStore.password
            keyAlias = AppConfig.KeyStore.alias
            keyPassword = AppConfig.KeyStore.password
        }
    }

    compileSdk = AppConfig.compileSdk

    defaultConfig {
        applicationId = "com.dirzaaulia.gamewish"
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = AppConfig.testInstrumentationRunner
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"

        // Enable Coroutines and Flow APIs
        freeCompilerArgs =  freeCompilerArgs +
                "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi" +
                "-Xopt-in=kotlinx.coroutines.FlowPreview" +
                "-Xopt-in=com.google.accompanist.pager.ExperimentalPagerApi" +
                "-Xopt-in=androidx.compose.foundation.ExperimentalFoundationApi" +
                "-Xopt-in=androidx.compose.material.ExperimentalMaterialApi" +
                "-Xopt-in=androidx.compose.animation.ExperimentalAnimationApi"
    }

    buildFeatures {
        compose = true
        // Disable unused AGP features
        buildConfig = false
        aidl = false
        renderScript = false
        resValues = false
        shaders = false
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Version.compose
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(Dependencies.Accompanist.implementation)
    implementation(Dependencies.AndroidX.implementation)
    implementation(Dependencies.AndroidX.Compose.implementation)
    implementation(Dependencies.AndroidX.Lifecycle.implementation)
    implementation(Dependencies.Coil.compose)
    implementation(Dependencies.Coroutines.implementation)
    implementation(Dependencies.DataStore.implementation)
    implementation(platform(Dependencies.Firebase.bom))
    implementation(Dependencies.Firebase.implementation)
    implementation(Dependencies.Firebase.UI.implementation)
    implementation(Dependencies.GMS.playServiceAuth)
    implementation(Dependencies.Gson.gson)
    implementation(Dependencies.Hilt.implementation)
    implementation(Dependencies.Kotlin.implementation)
    implementation(Dependencies.Material.material)
    implementation(Dependencies.Other.implementation)
    implementation(Dependencies.Paging.implementation)
    implementation(Dependencies.Protobuf.javalite)
    implementation(Dependencies.Room.implementation)
    implementation(Dependencies.SquareUp.implementation)

    kapt(Dependencies.Hilt.compiler)
    kapt(Dependencies.Room.compiler)

    debugImplementation(Dependencies.AndroidX.Compose.uiTestManifest)
    debugImplementation(Dependencies.Chucker.debug)

    releaseImplementation(Dependencies.Chucker.release)

    androidTestImplementation(Dependencies.AndroidX.activityCompose)
    androidTestImplementation(Dependencies.AndroidX.Compose.uiTest)
    androidTestImplementation(Dependencies.AndroidX.Test.testImplementation)
    androidTestImplementation(Dependencies.JUnit.junit)
}

protobuf {
    protoc {
        artifact = Dependencies.Protobuf.artifact
    }
    generateProtoTasks {
        all().forEach { task ->
            task.plugins.create("java") {
                option("lite")
            }
        }
    }
}