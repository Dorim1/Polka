package ru.anlyashenko.polkaapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso
import androidx.test.espresso.NoActivityResumedException
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.anlyashenko.core.data.repository.InventoryRepository
import javax.inject.Inject


/**
 * Tests all the navigation flows that are handled by the navigation library.
 */
@HiltAndroidTest
class NavigationTest {

    /**
     * Manages the components' state and is used to perform injection on your test
     */
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    /**
     * Use the primary activity to initialize the app normally.
     */
    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var inventoryRepository: InventoryRepository

    @Before
    fun setup() = hiltRule.inject()

    @Test
    fun firstScreen_isInventoryScreen() {
        composeTestRule.onNodeWithText("Polka").assertIsDisplayed()
    }

    @Test
    fun clickAuraIcon_navigatesToAuraScreen() {
        composeTestRule.onNodeWithContentDescription("Aura").performClick()
        composeTestRule.onNodeWithTag("aura:screen").assertIsDisplayed()
    }

    @Test
    fun backFromAura_returnsToInventory() {
        composeTestRule.onNodeWithContentDescription("Aura").performClick()
        Espresso.pressBack()
        composeTestRule.onNodeWithText("Polka").assertIsDisplayed()
    }

    @Test(expected = NoActivityResumedException::class)
    fun backFromInventory_quitsApp() {
        Espresso.pressBack()
    }

    @Test
    fun clickingCategoryFilter_showOnlyItsItems() {
        val inventory = runBlocking { inventoryRepository.getInventory().first() }
        val category = inventory.first().category
        val otherItem = inventory.last().items.first()

        composeTestRule.onNodeWithTag("category_filter_${category.id}")
            .performClick()

        composeTestRule.onNodeWithText(otherItem.name).assertDoesNotExist()
    }

}
