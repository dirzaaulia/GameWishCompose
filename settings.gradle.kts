pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
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
rootProject.name = "GameWish"
include(":app")
