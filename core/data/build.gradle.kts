plugins {
    alias(libs.plugins.polka.android.library)
    alias(libs.plugins.polka.hilt)
}

android {
    namespace = "ru.anlyashenko.core.data"
}

dependencies {
    implementation(projects.core.common)
    api(projects.core.database)
}
