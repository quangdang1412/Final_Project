plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.hcmute.quangvaphong"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.hcmute.quangvaphong"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("org.projectlombok:lombok:1.18.30")
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.translate)
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    implementation("androidx.room:room-runtime:2.5.2")
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.google.code.gson:gson:2.10.1")
    // ML Kit for OCR
    implementation("com.google.android.gms:play-services-mlkit-text-recognition:19.0.0")
    implementation("androidx.activity:activity-ktx:1.8.2")
    implementation("androidx.fragment:fragment:1.6.2")
    // JSoup for HTML parsing
    implementation("org.jsoup:jsoup:1.17.2")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    annotationProcessor("androidx.room:room-compiler:2.5.2")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("de.hdodenhof:circleimageview:3.1.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}