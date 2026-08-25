plugins {
    alias(libs.plugins.polka.android.feature.impl)
    alias(libs.plugins.polka.android.library.compose)
}

android {
    namespace = "ru.anlyashenko.feature.inventory.impl"
}

dependencies {
    implementation(projects.feature.inventory.api)
    implementation(projects.feature.aura.api)

    implementation(projects.core.data)

    testImplementation(libs.truth)
    testImplementation(libs.hilt.android.testing)
    testImplementation(libs.robolectric)
    testImplementation(projects.core.testing)
//  todo testDemoImplementation(projects.core.screenshotTesting)

    androidTestImplementation(libs.bundles.androidx.compose.ui.test)
    androidTestImplementation(projects.core.testing)

}
