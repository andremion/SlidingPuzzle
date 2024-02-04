/*
 *    Copyright 2024. André Luiz Oliveira Rêgo
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

plugins {
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
}

android {
    namespace = "io.github.andremion.slidingpuzzle.android"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "io.github.andremion.slidingpuzzle.android"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 3
        versionName = "0.1.0"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    val releaseKeyStoreFile = properties["releaseKeyStoreFile"]?.toString()
        ?: System.getenv("releaseKeyStoreFile")
    val releaseKeyStoreAlias = properties["releaseKeyStoreAlias"]?.toString()
        ?: System.getenv("releaseKeyStoreAlias")
    val releaseKeyStorePassword = properties["releaseKeyStorePassword"]?.toString()
        ?: System.getenv("releaseKeyStorePassword")
    val releaseKeysProvided =
        releaseKeyStoreFile != null && releaseKeyStoreAlias != null && releaseKeyStorePassword != null

    signingConfigs {
        if (releaseKeysProvided) {
            create("release") {
                storeFile = file(releaseKeyStoreFile)
                storePassword = releaseKeyStorePassword
                keyAlias = releaseKeyStoreAlias
                keyPassword = releaseKeyStorePassword
            }
        }
    }
    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
        release {
            if (releaseKeysProvided) {
                signingConfig = signingConfigs.getByName("release")
            }
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