plugins {
    alias(libs.plugins.polka.android.library)
    alias(libs.plugins.polka.hilt)
}

android {
    namespace = "ru.anlyashenko.core.testing"
}

dependencies {
    api(libs.kotlinx.coroutines.test)
    api(libs.turbine) // todo ?
    api(projects.core.common)
    api(projects.core.data)
    api(projects.core.model)

    implementation(libs.androidx.test.rules)
    implementation(libs.hilt.android.testing)
}
