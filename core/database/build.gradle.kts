plugins {
    alias(libs.plugins.polka.android.library)
    alias(libs.plugins.polka.android.room)
    alias(libs.plugins.polka.hilt)
}

android {
    namespace = "ru.anlyashenko.core.database"
}

dependencies {
    api(projects.core.model)

    implementation(projects.core.common)

    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.kotlinx.coroutines.test)

}
