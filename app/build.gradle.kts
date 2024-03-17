plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("dev.rikka.tools.materialthemebuilder") version "1.3.3"
}

android {
    namespace = "icu.takeneko.neko.medibox"
    compileSdk = 34

    defaultConfig {
        applicationId = "icu.takeneko.neko.medibox"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.utilcodex)
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx.v273)
    implementation(libs.androidx.navigation.ui.ktx.v273)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.gson)
    testImplementation(libs.junit)
}

materialThemeBuilder {
    themes {
        create("NekoPink") {
            lightThemeParent = "Theme.Material3.DayNight.NoActionBar"
            darkThemeParent = "Theme.Material3.DayNight.NoActionBar"
            lightThemeFormat = "Theme.Light.%s"
            darkThemeFormat = "Theme.Dark.%s"
            secondaryColor = "#F8BBD0"
            primaryColor = "#e91e64"
        }
    }
}