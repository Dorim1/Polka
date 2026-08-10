plugins {
    alias(libs.plugins.polka.android.feature.impl)
    alias(libs.plugins.polka.android.library.compose)
}

android {
    namespace = "ru.anlyashenko.feature.aura.impl"

}

dependencies {
    implementation(projects.feature.aura.api)

    implementation(projects.core.data)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}
