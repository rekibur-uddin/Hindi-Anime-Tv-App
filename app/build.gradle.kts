plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.hindianimetv"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.hindianimetv"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            debugSymbolLevel = "FULL"
            abiFilters += listOf("armeabi-v7a", "arm64-v8a")

        }
    }

    buildFeatures {
        viewBinding = true
        prefab = true
        buildConfig = true
    }

    buildTypes {
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            isDebuggable = true
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    ndkVersion = "29.0.14033849-rc4"

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    // Lifecycle / MVVM
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.4")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.9.4")

    // Coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

    // Retrofit + Gson
    implementation ("com.squareup.retrofit2:retrofit:3.0.0")
    implementation ("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:5.1.0")

    // Glide
    implementation ("com.github.bumptech.glide:glide:5.0.5")

    // Youtube player
    implementation ("com.pierfrancescosoffritti.androidyoutubeplayer:core:13.0.0")
}