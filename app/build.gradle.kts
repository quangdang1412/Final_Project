plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.hcmute.quangvaphong"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.hcmute.quangvaphong"
        minSdk = 30
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
}

dependencies {
    implementation ("org.projectlombok:lombok:1.18.30")
    annotationProcessor ("org.projectlombok:lombok:1.18.30")
    implementation("androidx.room:room-runtime:2.5.2")
      implementation("com.android.volley:volley:1.2.1")    
    implementation("com.google.code.gson:gson:2.10.1")
      // ML Kit for OCR
    implementation("com.google.android.gms:play-services-mlkit-text-recognition:19.0.0")
      // ML Kit for Translation
    implementation("com.google.mlkit:translate:17.0.1")
      // For handling images
    implementation("androidx.activity:activity-ktx:1.8.2")
    implementation("androidx.fragment:fragment:1.6.2")

    annotationProcessor ("androidx.room:room-compiler:2.5.2")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("de.hdodenhof:circleimageview:3.1.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}