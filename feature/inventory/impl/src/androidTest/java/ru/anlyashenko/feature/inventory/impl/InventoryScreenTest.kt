package ru.anlyashenko.feature.inventory.impl

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule
import ru.anlyashenko.core.designsystem.theme.PolkaAppTheme
import ru.anlyashenko.core.model.Category
import ru.anlyashenko.core.model.CategoryInventory
import ru.anlyashenko.core.model.Item

/**
 * UI test for checking the correct behaviour of the Inventory screen;
 * Verifies that, when a specific UiState is set, the corresponding
 * composables and details are shown
 */

class InventoryScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testCategory = Category(id = 1L, name = "Category1")
    private val testItemNormal = Item(
        id = 1L,
        categoryId = 1L,
        name = "Item1",
        icon = "🥕",
        quantity = 5.0,
        unit = "kg",
        expirationDate = null,
        storageConditions = null,
        needsAttention = false
    )
    private val testItemAttention = Item(
        id = 2L,
        categoryId = 1L,
        name = "Item2",
        icon = "🍅",
        quantity = 0.5,
        unit = "kg",
        expirationDate = null,
        storageConditions = null,
        needsAttention = true
    )

    private val successState = InventoryUiState.Success(
        filterCategories = listOf(testCategory),
        selectedCategory = null,
        inventory = listOf(
            CategoryInventory(
                category = testCategory,
                items = listOf(testItemNormal, testItemAttention)
            )
        )
    )

    @Test
    fun whenStateIsLoading_showsLoadingIndicator() {
        composeTestRule.setContent {
            InventoryScreenUnderTest(uiState = InventoryUiState.Loading)
        }

        composeTestRule
            .onNodeWithTag("loading_indicator")
            .assertIsDisplayed()
    }

    @Test
    fun whenStateIsSuccess_showsTopBarAndContent() {
        composeTestRule.setContent {
            InventoryScreenUnderTest(uiState = successState)
        }

        composeTestRule.onNodeWithText("Polka").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Back").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Aura").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("More").assertIsDisplayed()

        composeTestRule.onAllNodesWithText("Category1")[0].assertIsDisplayed()

        composeTestRule.onNodeWithText("Item1").assertIsDisplayed()
        composeTestRule.onNodeWithText("5 kg").assertIsDisplayed()

        composeTestRule.onNodeWithText("Item2").assertIsDisplayed()
        composeTestRule.onNodeWithText("! 0.5 kg").assertIsDisplayed()
    }

    @Test
    fun clickingItem_triggersItemClickCallbackWithCorrectItem() {
        var clickedItem: Item? = null

        composeTestRule.setContent {
            InventoryScreenUnderTest(
                uiState = successState,
                onItemClick = { item -> clickedItem = item }
            )
        }

        composeTestRule
            .onNodeWithText("Item1")
            .performClick()

        assertEquals(testItemNormal, clickedItem)
    }

    @Test
    fun clickingCategoryFilter_triggersCategoryClickCallback() {
        var clickedCategory: Category? = null

        composeTestRule.setContent {
            InventoryScreenUnderTest(
                uiState = successState,
                onCategoryFilterClick = { category -> clickedCategory = category }
            )
        }

        composeTestRule
            .onAllNodesWithText("Category1")[0]
            .performClick()

        assertEquals(testCategory, clickedCategory)
    }

    @Test
    fun clickingTopBarIcons_triggersCorrespondingCallbacks() {
        var backClicked = false
        var auraClicked = false
        var moreClicked = false

        composeTestRule.setContent {
            InventoryScreenUnderTest(
                uiState = successState,
                onBackClick = { backClicked = true },
                onAuraClick = { auraClicked = true },
                onMoreClick = { moreClicked = true },
            )
        }

        composeTestRule.onNodeWithContentDescription("Back").performClick()
        composeTestRule.onNodeWithContentDescription("Aura").performClick()
        composeTestRule.onNodeWithContentDescription("More").performClick()

        assertTrue(backClicked)
        assertTrue(auraClicked)
        assertTrue(moreClicked)
    }

    @Composable
    private fun InventoryScreenUnderTest(
        uiState: InventoryUiState,
        onBackClick: () -> Unit = {},
        onMoreClick: () -> Unit = {},
        onAuraClick: () -> Unit = {},
        onCategoryFilterClick: (Category) -> Unit = {},
        onItemClick: (Item) -> Unit = {}
    ) {
        PolkaAppTheme {
            InventoryScreen(
                uiState = uiState,
                onBackClick = onBackClick,
                onMoreClick = onMoreClick,
                onAuraClick = onAuraClick,
                onCategoryFilterClick = onCategoryFilterClick,
                onItemClick = onItemClick,
            )
        }
    }

}
