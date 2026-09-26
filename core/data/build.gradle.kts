plugins {
    alias(libs.plugins.polka.android.library)
    alias(libs.plugins.polka.hilt)
}

android {
    namespace = "ru.anlyashenko.core.data"
}

dependencies {
    api(projects.core.common)
    api(projects.core.database)

    testImplementation(libs.truth)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(projects.core.testing)
}
