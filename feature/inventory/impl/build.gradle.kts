plugins {
    alias(libs.plugins.polka.android.feature.impl)
    alias(libs.plugins.polka.android.library.compose)
}

android {
    namespace = "ru.anlyashenko.feature.inventory.impl"
}

dependencies {
    implementation(projects.feature.inventory.api)

    implementation(projects.core.data)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}
