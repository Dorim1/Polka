plugins {
    alias(libs.plugins.polka.android.feature.impl)
    alias(libs.plugins.polka.android.library.compose)
    alias(libs.plugins.roborazzi)
}

android {
    namespace = "ru.anlyashenko.feature.inventory.impl"
    testOptions.unitTests.isIncludeAndroidResources = true

}

dependencies {
    implementation(projects.feature.inventory.api)
    implementation(projects.feature.aura.api)

    implementation(projects.core.data)

    testImplementation(libs.truth)
    testImplementation(libs.hilt.android.testing)
    testImplementation(libs.robolectric)
    testImplementation(projects.core.testing)
    testImplementation(projects.core.screenshotTesting)

    androidTestImplementation(libs.bundles.androidx.compose.ui.test)
    androidTestImplementation(libs.androidx.espresso.core) // Вынести в convention plugin
    androidTestImplementation(projects.core.testing)

}
