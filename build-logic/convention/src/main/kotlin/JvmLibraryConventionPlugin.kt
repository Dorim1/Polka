import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import ru.anlyashenko.polkaapp.configureKotlinJvm
import ru.anlyashenko.polkaapp.configureSpotlessForJvm
import ru.anlyashenko.polkaapp.libs

abstract class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "org.jetbrains.kotlin.jvm")
            apply(plugin = "polka.android.lint")

            configureKotlinJvm()
            configureSpotlessForJvm()
            dependencies {
                "testImplementation"(libs.findLibrary("kotlin.test").get())
            }
        }
    }
}
