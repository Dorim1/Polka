plugins {
    alias(libs.plugins.polka.android.application)
    alias(libs.plugins.polka.android.application.compose)
    alias(libs.plugins.polka.hilt)
}

android {
    namespace = "ru.anlyashenko.polkaapp"

    defaultConfig {
        applicationId = "ru.anlyashenko.polkaapp"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
}

dependencies {
    implementation(projects.feature.inventory.api)
    implementation(projects.feature.inventory.impl)

    implementation(projects.feature.aura.api)
    implementation(projects.feature.aura.impl)

    implementation(projects.core.designsystem)
    implementation(projects.core.data)
    implementation(projects.core.model)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.compose.lifecycle)
    implementation(libs.androidx.lifecycle.viewModel.navigation3)
    implementation(libs.kotlinx.serialization.converter)

    ksp(libs.hilt.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}
