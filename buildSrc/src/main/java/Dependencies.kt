import org.gradle.api.artifacts.dsl.DependencyHandler

object Dependencies {

    object Accompanist {
        private const val animation = "com.google.accompanist:accompanist-navigation-animation:${Version.accompanist}"
        private const val flowLayout = "com.google.accompanist:accompanist-flowlayout:${Version.accompanist}"
        private const val insets = "com.google.accompanist:accompanist-insets:${Version.accompanist}"
        private const val pager = "com.google.accompanist:accompanist-pager:${Version.accompanist}"
        private const val placeholderUI = "com.google.accompanist:accompanist-placeholder-material:${Version.accompanist}"
        private const val swipeRefresh = "com.google.accompanist:accompanist-swiperefresh:${Version.accompanist}"
        private const val systemUiController = "com.google.accompanist:accompanist-systemuicontroller:${Version.accompanist}"
        private const val web = "com.google.accompanist:accompanist-webview:${Version.accompanist}"

        val implementation = arrayListOf<String>().apply {
            add(animation)
            add(flowLayout)
            add(insets)
            add(pager)
            add(placeholderUI)
            add(swipeRefresh)
            add(systemUiController)
            add(web)
        }
    }

    object AndroidX {
        private const val activityCompose = "androidx.activity:activity-compose:${Version.activityCompose}"
        private const val constraintLayoutCompose =
            "androidx.constraintlayout:constraintlayout-compose:${Version.constraintLayoutCompose}"
        private const val coreKtx = "androidx.core:core-ktx:${Version.coreKtx}"
        private const val splashScreen = "androidx.core:core-splashscreen:${Version.coreSplashScreen}"
        private const val navigation = "androidx.navigation:navigation-compose:${Version.navigationCompose}"
        private const val webkit = "androidx.webkit:webkit:${Version.webkit}"

        val implementation = arrayListOf<String>().apply {
            add(activityCompose)
            add(constraintLayoutCompose)
            add(coreKtx)
            add(splashScreen)
            add(navigation)
            add(webkit)
        }

        object Compose {
            private const val animation = "androidx.compose.animation:animation:${Version.compose}"
            private const val foundation = "androidx.compose.foundation:foundation:${Version.compose}"
            private const val iconsExtended = "androidx.compose.material:material-icons-extended:${Version.compose}"
            private const val layout = "androidx.compose.foundation:foundation-layout:${Version.compose}"
            private const val livedata = "androidx.compose.runtime:runtime-livedata:${Version.compose}"
            private const val material = "androidx.compose.material:material:${Version.compose}"
            private const val runtime = "androidx.compose.runtime:runtime:${Version.compose}"
            private const val tooling = "androidx.compose.ui:ui-tooling:${Version.compose}"
            private const val ui = "androidx.compose.ui:ui:${Version.compose}"
            private const val uiUtil = "androidx.compose.ui:ui-util:${Version.compose}"
            private const val uiTest = "androidx.compose.ui:ui-test-junit4:${Version.compose}"
            const val uiTestManifest = "androidx.compose.ui:ui-test-manifest:${Version.compose}"

            val implementation = arrayListOf<String>().apply {
                add(animation)
                add(foundation)
                add(iconsExtended)
                add(layout)
                add(livedata)
                add(material)
                add(runtime)
                add(tooling)
                add(ui)
                add(uiUtil)
            }

            val androidTestImplementation = arrayListOf<String>().apply {
                add(uiTest)
            }

            val debugImplementation = arrayListOf<String>().apply {
                add(uiTestManifest)
            }
        }

        object Lifecycle {
            private const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:${Version.lifecycle}"

            val implementation = arrayListOf<String>().apply {
                add(viewModel)
            }
        }

        object Test {
            private const val core = "androidx.test:core:${Version.test}"
            private const val rules = "androidx.test:rules:${Version.test}"
            private const val jUnit = "androidx.test.ext:junit-ktx:${Version.extJUnit}"
            private const val espressoCore = "androidx.test.espresso:espresso-core:${Version.espresso}"

            val androidTestImplementation = arrayListOf<String>().apply {
                add(core)
                add(rules)
                add(jUnit)
                add(espressoCore)
            }
        }
    }

    object Chucker {
        private const val debug = "com.github.chuckerteam.chucker:library:${Version.chucker}"
        private const val release = "com.github.chuckerteam.chucker:library-no-op:${Version.chucker}"

        val debugImplementation = arrayListOf<String>().apply {
            add(debug)
        }

        val releaseImplementation = arrayListOf<String>().apply {
            add(release)
        }
    }

    object Coil {
        private const val coil = "io.coil-kt:coil-compose:${Version.coil}"

        val implementation = arrayListOf<String>().apply {
            add(coil)
        }
    }

    object Coroutines {
        private const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.coroutines}"
        private const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.coroutines}"
        private const val playServices = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Version.coroutines}"

        val implementation = arrayListOf<String>().apply {
            add(android)
            add(core)
            add(playServices)
        }
    }

    object DataStore {
        private const val core = "androidx.datastore:datastore-core:${Version.datastore}"
        private const val main = "androidx.datastore:datastore:${Version.datastore}"

        val implementation = arrayListOf<String>().apply {
            add(core)
            add(main)
        }
    }

    object Firebase {
        const val bom = "com.google.firebase:firebase-bom:${Version.firebase}"
        private const val analytics = "com.google.firebase:firebase-analytics-ktx"
        private const val auth = "com.google.firebase:firebase-auth-ktx"
        private const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
        private const val database = "com.google.firebase:firebase-database-ktx"
        private const val firestore = "com.google.firebase:firebase-firestore-ktx"
        private const val storage = "com.google.firebase:firebase-storage-ktx"

        val implementation = arrayListOf<String>().apply {
            add(analytics)
            add(auth)
            add(crashlytics)
            add(database)
            add(firestore)
            add(storage)
        }

        object UI {
            private const val auth = "com.firebaseui:firebase-ui-auth:${Version.firebaseUI}"
            private const val database = "com.firebaseui:firebase-ui-database:${Version.firebaseUI}"

            val implementation = arrayListOf<String>().apply {
                add(auth)
                add(database)
            }
        }
    }

    object GMS {
        private const val playServiceAuth = "com.google.android.gms:play-services-auth:${Version.gmsPlayServicesAuth}"

        val implementation = arrayListOf<String>().apply {
            add(playServiceAuth)
        }
    }

    object Gson {
        private const val gson = "com.google.code.gson:gson:${Version.gson}"

        val implementation = arrayListOf<String>().apply {
            add(gson)
        }
    }

    object Hilt {
        private const val android = "com.google.dagger:hilt-android:${Version.hilt}"
        private const val compiler = "com.google.dagger:hilt-android-compiler:${Version.hilt}"
        private const val navigation = "androidx.hilt:hilt-navigation-compose:${Version.hiltCompose}"

        val implementation = arrayListOf<String>().apply {
            add(android)
            add(navigation)
        }

        val kapt = arrayListOf<String>().apply {
            add(compiler)
        }
    }

    object JUnit {
        private const val junit = "junit:junit:${Version.jUnit}"

        val androidTestImplementation = arrayListOf<String>().apply {
            add(junit)
        }
    }

    object Kotlin {
        private const val reflect = "org.jetbrains.kotlin:kotlin-reflect:${Version.reflect}"
        private const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Version.kotlin}"

        val implementation = arrayListOf<String>().apply {
            add(reflect)
            add(stdlib)
        }
    }

    object Material {
        private const val material = "com.google.android.material:material:${Version.material}"

        val implementation = arrayListOf<String>().apply {
            add(material)
        }
    }

    object Other {
        private const val currencyConverter = "com.github.vinisauter:currencyconverter:1.0"
        private const val imageSlideShow = "com.github.denzcoskun:imageslideshow:0.1.0"
        private const val photoView = "com.github.chrisbanes:photoview:2.2.0"
        private const val timber = "com.jakewharton.timber:timber:4.7.1"

        val implementation = arrayListOf<String>().apply {
            add(currencyConverter)
            add(imageSlideShow)
            add(photoView)
            add(timber)
        }
    }

    object Paging {
        private const val compose = "androidx.paging:paging-compose:${Version.pagingCompose}"
        private const val runtime = "androidx.paging:paging-runtime-ktx:${Version.paging}"

        val implementation = arrayListOf<String>().apply {
            add(compose)
            add(runtime)
        }
    }

    object Protobuf {
        private const val javalite = "com.google.protobuf:protobuf-javalite:${Version.protobuf}"
        const val artifact = "com.google.protobuf:protoc:${Version.protobuf}"

        val implementation = arrayListOf<String>().apply {
            add(javalite)
        }
    }

    object Room {
        private const val compiler = "androidx.room:room-compiler:${Version.room}"
        private const val ktx = "androidx.room:room-ktx:${Version.room}"
        private const val paging = "androidx.room:room-paging:${Version.room}"
        private const val runtime = "androidx.room:room-runtime:${Version.room}"

        val implementation = arrayListOf<String>().apply {
            add(ktx)
            add(paging)
            add(runtime)
        }

        val kapt = arrayListOf<String>().apply {
            add(compiler)
        }
    }

    object SquareUp {
        private const val core = "com.squareup.retrofit2:converter-moshi:${Version.retrofit}"
        private const val kotlin = "com.squareup.moshi:moshi-kotlin:${Version.moshi}"
        private const val logging = "com.squareup.okhttp3:logging-interceptor:${Version.okHttp}"

        val implementation = arrayListOf<String>().apply {
            add(core)
            add(kotlin)
            add(logging)
        }
    }
}

fun DependencyHandler.kapt(list: List<String>) {
    list.forEach { dependency ->
        add("kapt", dependency)
    }
}

fun DependencyHandler.implementation(list: List<String>) {
    list.forEach { dependency ->
        add("implementation", dependency)
    }
}

fun DependencyHandler.androidTestImplementation(list: List<String>) {
    list.forEach { dependency ->
        add("androidTestImplementation", dependency)
    }
}

fun DependencyHandler.debugImplementation(list: List<String>) {
    list.forEach { dependency ->
        add("debugImplementation", dependency)
    }
}

fun DependencyHandler.releaseImplementation(list: List<String>) {
    list.forEach { dependency ->
        add("releaseImplementation", dependency)
    }
}