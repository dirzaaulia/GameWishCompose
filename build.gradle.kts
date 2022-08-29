buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:${Version.toolsBuildGradle}")
        classpath("com.google.firebase:firebase-crashlytics-gradle:${Version.crashlyticsGradle}")
        classpath("com.google.gms:google-services:${Version.googleMapsServices}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Version.hiltGradle}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlinGradle}")
        classpath("com.google.protobuf:protobuf-gradle-plugin:${Version.protobufGradle}")
        classpath("com.diffplug.spotless:spotless-plugin-gradle:${Version.spotless}")
    }
}

subprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://www.jitpack.io") }
        maven {
            url = uri("https://jitpack.io")
            credentials {
                username = "jp_uvj8t2utirothfevp7gb6l9udn"
            }
        }
    }
}