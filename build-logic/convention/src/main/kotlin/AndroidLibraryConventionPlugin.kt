import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import ru.anlyashenko.polkaapp.configureKotlinAndroid
import ru.anlyashenko.polkaapp.configureSpotlessForAndroid
import ru.anlyashenko.polkaapp.disableUnnecessaryAndroidTests
import ru.anlyashenko.polkaapp.libs
import kotlin.text.get

abstract class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.library")
            apply(plugin = "polka.android.lint")

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                testOptions.targetSdk = 37
                lint.targetSdk = 37
                defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                testOptions.animationsDisabled = true
                /*resourcePrefix =
                    path.split("""\W""".toRegex()).drop(1).distinct().joinToString(separator = "_")
                        .lowercase() + "_"*/
            }

            extensions.configure<LibraryAndroidComponentsExtension> {
                // Отключаем тесты, если нет папки src/androidTest
                disableUnnecessaryAndroidTests(target)
            }
            configureSpotlessForAndroid()

            dependencies {
                "androidTestImplementation"(libs.findLibrary("kotlin.test").get())
                "testImplementation"(libs.findLibrary("kotlin.test").get())
                "testImplementation"(libs.findLibrary("junit").get())

                "implementation"(libs.findLibrary("androidx.tracing.ktx").get())
            }
        }
    }
}
