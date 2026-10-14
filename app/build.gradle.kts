plugins {
    alias(libs.plugins.polka.android.application)
    alias(libs.plugins.polka.android.application.compose)
    alias(libs.plugins.polka.hilt)
    alias(libs.plugins.roborazzi)
}

android {
    defaultConfig {
        applicationId = "ru.anlyashenko.polkaapp"
        versionCode = 1
        versionName = "1.0"

        // Custom test runner to set up Hilt dependency graph
        testInstrumentationRunner = "ru.anlyashenko.core.testing.PolkaTestRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }

    namespace = "ru.anlyashenko.polkaapp"
    testOptions.unitTests.isIncludeAndroidResources = true
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
    implementation(libs.androidx.compose.runtime.tracing)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.tracing.ktx)
    implementation(libs.androidx.compose.lifecycle)
    implementation(libs.androidx.lifecycle.viewModel.navigation3)
    implementation(libs.kotlinx.serialization.converter)

    ksp(libs.hilt.compiler)

    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(projects.uiTestHiltManifest)

    kspTest(libs.hilt.compiler)

    testImplementation(libs.hilt.android.testing)
//    testImplementation(projects.core.dataTest)
    testImplementation(libs.kotlin.test)

    /*testDemoImplementation(libs.androidx.navigation.testing)
    testDemoImplementation(libs.robolectric)
    testDemoImplementation(libs.roborazzi)
    testDemoImplementation(projects.core.screenshotTesting)
    testDemoImplementation(projects.core.testing)*/

    androidTestImplementation(projects.core.testing)
//    androidTestImplementation(projects.core.dataTest)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.kotlin.test)
}
