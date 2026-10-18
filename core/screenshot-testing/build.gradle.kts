plugins {
    alias(libs.plugins.polka.android.library)
    alias(libs.plugins.polka.android.library.compose)
    alias(libs.plugins.polka.hilt)
}

android {
    namespace = "ru.anlyashenko.core.screenshot_testing"
}

dependencies {
    api(libs.bundles.androidx.compose.ui.test)
    api(libs.roborazzi)
    api(libs.roborazzi.accessibility.check)
    implementation(libs.androidx.compose.ui.test.junit4)
    implementation(libs.androidx.activity.compose)
    implementation(libs.robolectric)
    implementation(projects.core.designsystem)
}
