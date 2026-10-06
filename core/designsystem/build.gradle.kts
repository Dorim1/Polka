plugins {
    alias(libs.plugins.polka.android.library)
    alias(libs.plugins.polka.android.library.compose)
    alias(libs.plugins.roborazzi)
}

android {
    namespace = "ru.anlyashenko.core.designsystem"
    testOptions.unitTests.isIncludeAndroidResources = true

}

dependencies {
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.runtime)

    implementation(projects.core.model)

    testImplementation(libs.androidx.compose.ui.test.junit4)
    testImplementation(libs.androidx.compose.ui.test.manifest)

    testImplementation(libs.hilt.android.testing)
    testImplementation(libs.robolectric)
    testImplementation(projects.core.screenshotTesting)

}
