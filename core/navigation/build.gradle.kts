plugins {
    alias(libs.plugins.polka.android.library)
    alias(libs.plugins.polka.hilt)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "ru.anlyashenko.core.navigation"
}

dependencies {
    api(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewModel.navigation3)

    testImplementation(libs.truth)

    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.compose.ui.test.manifest)
    androidTestImplementation(libs.androidx.lifecycle.viewModel.testing)
    androidTestImplementation(libs.truth)

}
