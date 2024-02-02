plugins {
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "io.github.andremion.slidingpuzzle.android"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "io.github.andremion.slidingpuzzle.android"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 2
        versionName = "1.0.0"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    signingConfigs {
        val default = "NOT_PROVIDED"
        val releaseKeyStoreFile: String? by project
        val releaseKeyStoreAlias: String? by project
        val releaseKeyStorePassword: String? by project
        create("release") {
            val storeFilePath = releaseKeyStoreFile ?: System.getenv("releaseKeyStoreFile") ?: default
            if (storeFilePath != default) {
                storeFile = file(storeFilePath)
            }
            storePassword = releaseKeyStorePassword ?: System.getenv("releaseKeyStorePassword") ?: default
            keyAlias = releaseKeyStoreAlias ?: System.getenv("releaseKeyStoreAlias") ?: default
            keyPassword = releaseKeyStorePassword ?: System.getenv("releaseKeyStorePassword") ?: default
        }
    }
    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
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
    implementation(projects.shared)

    debugImplementation(compose.uiTooling)
    debugImplementation(compose.preview)
}