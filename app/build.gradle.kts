plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
}

android {
    namespace = "com.seftian.bnicasestudies"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.seftian.bnicasestudies"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":core"))

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // hilt
    implementation("com.google.dagger:hilt-android:2.45")
    kapt("com.google.dagger:hilt-android-compiler:2.45")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation("androidx.navigation:navigation-compose:2.7.2")

    //  viewmodel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

    // live data
    implementation("androidx.compose.runtime:runtime-livedata:1.5.1")

    // to show image from url
    implementation("io.coil-kt:coil-compose:2.4.0")

    // navigation
    implementation("androidx.navigation:navigation-compose: 2.7.1")

    // permission requester
    implementation("com.google.accompanist:accompanist-permissions:0.33.1-alpha")

    val cameraxversion = "1.2.3"
    implementation ("androidx.camera:camera-core:${cameraxversion}")
    implementation ("androidx.camera:camera-camera2:${cameraxversion}")
    implementation ("androidx.camera:camera-lifecycle:${cameraxversion}")
    implementation ("androidx.camera:camera-view:1.3.0-rc01")

    //Barcode
    implementation("com.google.mlkit:barcode-scanning:17.2.0")

    // to show image from url
    implementation("io.coil-kt:coil-compose:2.4.0")

    // extended icon
    implementation ("androidx.compose.material:material-icons-extended:1.5.1")
}