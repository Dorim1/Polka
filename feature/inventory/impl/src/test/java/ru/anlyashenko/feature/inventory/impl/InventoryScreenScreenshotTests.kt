package ru.anlyashenko.feature.inventory.impl

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import com.ibm.icu.util.TimeZone
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.annotation.LooperMode
import ru.anlyashenko.core.designsystem.component.PolkaBackground
import ru.anlyashenko.core.designsystem.theme.PolkaAppTheme
import ru.anlyashenko.core.screenshot_testing.DefaultTestDevices
import ru.anlyashenko.core.screenshot_testing.captureForDevice
import ru.anlyashenko.core.screenshot_testing.captureMultiDevice
import ru.anlyashenko.core.ui.InventoryListPreviewParameterProvider

/**
 * Screenshot tests for the [InventoryScreen].
 */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = HiltTestApplication::class, sdk = [35]) // todo: убрать sdk
@LooperMode(LooperMode.Mode.PAUSED)
class InventoryScreenScreenshotTests {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val sampleInventory = InventoryListPreviewParameterProvider().values.first()

    @Before
    fun setTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    }

    @Test
    fun inventoryScreenPopulated() {
        composeTestRule.captureMultiDevice("InventoryScreenPopulated") {
            InventoryScreenPopulatedContent()
        }
    }

    @Test
    fun inventoryScreenPopulated_dark() {
        composeTestRule.captureForDevice(
            deviceName = "phone_dark",
            deviceSpec = DefaultTestDevices.PHONE.spec,
            screenshotName = "InventoryScreenPopulated",
            darkMode = true,
        ) {
            InventoryScreenPopulatedContent()
        }
    }

    @Test
    fun inventoryScreenLoading() {
        composeTestRule.captureMultiDevice("InventoryScreenLoading") {
            InventoryScreenLoadingContent()
        }
    }

    @Test
    fun inventoryScreenLoading_dark() {
        composeTestRule.captureForDevice(
            deviceName = "phone_dark",
            deviceSpec = DefaultTestDevices.PHONE.spec,
            screenshotName = "InventoryScreenLoading",
            darkMode = true,
        ) {
            InventoryScreenLoadingContent()
        }
    }

    @Composable
    private fun InventoryScreenPopulatedContent() {
        PolkaAppTheme() {
            PolkaBackground() {
                InventoryScreen(
                    uiState = InventoryUiState.Success(
                        inventory = sampleInventory,
                        filterCategories = sampleInventory.map { it.category }
                    ),
                    onBackClick = { },
                    onMoreClick = { },
                    onAuraClick = { },
                    onCategoryFilterClick = { },
                    onItemClick = { },
                )
            }
        }
    }

    @Composable
    private fun InventoryScreenLoadingContent() {
        PolkaAppTheme() {
            PolkaBackground() {
                InventoryScreen(
                    uiState = InventoryUiState.Loading,
                    onBackClick = { },
                    onMoreClick = { },
                    onAuraClick = { },
                    onCategoryFilterClick = { },
                    onItemClick = { },
                )
            }
        }
    }
}
