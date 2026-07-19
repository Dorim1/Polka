plugins {
    alias(libs.plugins.polka.jvm.library)
    alias(libs.plugins.polka.hilt)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.jakarta.inject.api)
}
