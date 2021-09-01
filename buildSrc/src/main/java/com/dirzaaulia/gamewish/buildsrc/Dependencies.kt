package com.dirzaaulia.gamewish.buildsrc

object Versions {
    const val ktlint = "0.41.0"
}

object Libs {
    const val androidGradlePlugin = "com.android.tools.build:gradle:7.0.1"

    object Accompanist {
        const val version = "0.16.0"
        const val animation = "com.google.accompanist:accompanist-navigation-animation:$version"
        const val insets = "com.google.accompanist:accompanist-insets:$version"
        const val swipeRefresh = "com.google.accompanist:accompanist-swiperefresh:$version"
        const val systemUiController = "com.google.accompanist:accompanist-systemuicontroller:$version"
    }

    object AndroidX {
        const val coreKtx = "androidx.core:core-ktx:1.6.0"
        const val splashScreen = "androidx.core:core-splashscreen:1.0.0-alpha01"
        const val navigation = "androidx.navigation:navigation-compose:2.4.0-alpha06"


        object Activity {
            const val activityCompose = "androidx.activity:activity-compose:1.3.1"
        }

        object Compose {
            const val snapshot = ""
            const val version = "1.0.1"

            const val animation = "androidx.compose.animation:animation:$version"
            const val foundation = "androidx.compose.foundation:foundation:$version"
            const val iconsExtended = "androidx.compose.material:material-icons-extended:$version"
            const val layout = "androidx.compose.foundation:foundation-layout:$version"
            const val livedata = "androidx.compose.runtime:runtime-livedata:$version"
            const val material = "androidx.compose.material:material:$version"
            const val runtime = "androidx.compose.runtime:runtime:$version"
            const val tooling = "androidx.compose.ui:ui-tooling:$version"
            const val ui = "androidx.compose.ui:ui:$version"
            const val uiUtil = "androidx.compose.ui:ui-util:$version"
            const val uiTest = "androidx.compose.ui:ui-test-junit4:$version"
            const val uiTestManifest = "androidx.compose.ui:ui-test-manifest:$version"
        }

        object Lifecycle {
            private const val version = "2.3.1"

            const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
            const val viewModel_savedState = "androidx.lifecycle:lifecycle-viewmodel-savedstate:$version"
        }

        object LifecycleCompose {
            private const val version = "1.0.0-alpha07"
            const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:$version"
        }

        object ConstraintLayout {
            const val constraintLayoutCompose =
                "androidx.constraintlayout:constraintlayout-compose:1.0.0-beta02"
        }

        object Test {
            private const val version = "1.3.0"
            const val core = "androidx.test:core:$version"
            const val rules = "androidx.test:rules:$version"

            object Ext {
                private const val version = "1.1.2"
                const val junit = "androidx.test.ext:junit-ktx:$version"
            }

            const val espressoCore = "androidx.test.espresso:espresso-core:3.3.0"
        }
    }

    object Chucker {
        private const val version = "3.5.2"

        const val debug = "com.github.chuckerteam.chucker:library:$version"
        const val release = "com.github.chuckerteam.chucker:library-no-op:$version"
    }

    object Coil {
        const val coilCompose = "io.coil-kt:coil-compose:1.3.0"
    }

    object Coroutines {
        private const val version = "1.5.1"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object Firebase {
        private const val version = "28.3.1"

        const val bom = "com.google.firebase:firebase-bom:$version"
        const val database = "com.google.firebase:firebase-database-ktx"
        const val storage = "com.google.firebase:firebase-storage-ktx"
        const val auth = "com.google.firebase:firebase-auth-ktx"

        object UI {
            private const val version = "8.0.0"

            const val auth = "com.firebaseui:firebase-ui-auth:$version"
            const val database = "com.firebaseui:firebase-ui-database:$version"
        }
    }

    object Gms {
        const val plugin = "com.google.gms:google-services:4.3.10"
        private const val version = "19.2.0"

        const val playServiceAuth = "com.google.android.gms:play-services-auth:$version"
    }

    object Gson {
        private const val version = "2.8.7"

        const val core = "com.google.code.gson:gson:$version"
    }

    object Hilt {
        private const val version = "2.38.1"

        const val androidGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:$version"
        const val android = "com.google.dagger:hilt-android:$version"
        const val compiler = "com.google.dagger:hilt-android-compiler:$version"

        object Compose {
            private const val version = "1.0.0-alpha03"

            const val navigation = "androidx.hilt:hilt-navigation-compose:$version"
        }
    }

    object JUnit {
        private const val version = "4.13"
        const val junit = "junit:junit:$version"
    }

    object Kotlin {
        private const val version = "1.5.21"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"
    }

    object Other {
        private const val version = "0.1.0"

        const val imageSlideShow = "com.github.denzcoskun:imageslideshow:$version"
        const val photoView = "com.github.chrisbanes:photoview:2.2.0"
        const val timber = "com.jakewharton.timber:timber:4.7.1"
        const val currencyConverter = "com.github.vinisauter:currencyconverter:1.0"
    }

    object Paging {
        private const val version = "1.0.0-alpha12"

        const val compose = "androidx.paging:paging-compose:$version"
    }

    object Proto {
        private const val version = "1.0.0"

        const val main = "androidx.datastore:datastore:$version"
        const val core = "androidx.datastore:datastore-core:$version"

        object Protobuf {
            private const val version = "3.14.0"

            const val javalite = "com.google.protobuf:protobuf-javalite:$version"
            const val artifact = "com.google.protobuf:protoc:3.10.0"
        }
    }

    object Room {
        private const val version = "2.3.0"

        const val compiler = "androidx.room:room-compiler:$version"
        const val ktx = "androidx.room:room-ktx:$version"
        const val runtime = "androidx.room:room-runtime:$version"
    }

    object SquareUp {

        object Retrofit {
            private const val version = "2.9.0"

            const val core = "com.squareup.retrofit2:converter-moshi:$version"
        }

        object Moshi {
            private const val version = "1.11.0"

            const val kotlin = "com.squareup.moshi:moshi-kotlin:$version"
        }

        object OkHttp {
            private const val version = "5.0.0-alpha.2"

            const val logging = "com.squareup.okhttp3:logging-interceptor:$version"
        }
    }
}