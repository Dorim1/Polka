plugins {
    alias(libs.plugins.polka.android.library)
    alias(libs.plugins.polka.android.library.compose)
}

android {
    namespace = "ru.anlyashenko.core.ui"

}

dependencies {
    api(projects.core.designsystem)
    api(projects.core.model)
}
