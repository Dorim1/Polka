plugins {
    alias(libs.plugins.polka.android.library)
    alias(libs.plugins.polka.android.library.compose)
}

android {
    namespace = "ru.anlyashenko.core.designsystem"

}

dependencies {
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.runtime)

    implementation(projects.core.model)
}
