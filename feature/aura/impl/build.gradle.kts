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

    testImplementation(projects.core.testing)

    androidTestImplementation(libs.bundles.androidx.compose.ui.test)
    androidTestImplementation(projects.core.testing)
}
