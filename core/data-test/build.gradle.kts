plugins {
    alias(libs.plugins.polka.android.library)
    alias(libs.plugins.polka.hilt)
}

android {
    namespace = "ru.anlyashenko.data_test"
}

dependencies {
    api(projects.core.data)

    implementation(libs.hilt.android.testing)
}
