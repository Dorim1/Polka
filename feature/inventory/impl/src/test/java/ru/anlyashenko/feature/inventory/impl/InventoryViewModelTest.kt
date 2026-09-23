package ru.anlyashenko.feature.inventory.impl

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.anlyashenko.core.model.Category
import ru.anlyashenko.core.model.CategoryInventory
import ru.anlyashenko.core.model.Item
import ru.anlyashenko.core.testing.repository.TestInventoryRepository
import ru.anlyashenko.core.testing.util.MainDispatcherRule

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class InventoryViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val inventoryRepository = TestInventoryRepository()

    private lateinit var viewModel: InventoryViewModel

    @Before
    fun setup() {
        viewModel = InventoryViewModel(
            inventoryRepository = inventoryRepository,
            // savedStateHandle = SavedStateHandle() // todo: if needed later
        )
    }

    /**
     * Verifies that the initial UI state of the ViewModel is [InventoryUiState.Loading].
     */
    @Test
    fun stateIsInitiallyLoading() = runTest {
        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(InventoryUiState.Loading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * Verifies that when the repository emits inventory data, the UI state updates to
     * [InventoryUiState.Success] containing the correct data and no pre-selected category.
     */
    @Test
    fun stateIsSuccessWhenInventoryLoads() = runTest {
        viewModel.uiState.test {
            // Skip initial Loading state
            assertThat(awaitItem()).isEqualTo(InventoryUiState.Loading)

            val sampleCategory = Category(
                id = 1L,
                name = "Sample Category",
            )

            val sampleItem = Item(
                id = 10L,
                categoryId = sampleCategory.id,
                name = "Sample Item",
                icon = null,
                quantity = 1.0,
                unit = "kg",
                expirationDate = null,
                storageConditions = null,
                needsAttention = false,

                )

            val testCategories = listOf(
                CategoryInventory(
                    category = sampleCategory,
                    items = listOf(sampleItem),
                )
            )

            // Emit mocked data from the fake repository
            inventoryRepository.sendInventory(testCategories)

            // Verify Success state
            val successState = awaitItem() as InventoryUiState.Success

            assertThat(successState.inventory).isEqualTo(testCategories)
            assertThat(successState.filterCategories).containsExactly(sampleCategory)
            assertThat(successState.selectedCategory).isNull()

            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * Verifies the behavior of the category filter:
     * 1. Selecting a category filters the inventory list.
     * 2. Selecting the same category again clears the filter and restores the full list.
     */
    @Test
    fun categoryFilterTogglesAndFiltersInventory() = runTest {
        viewModel.uiState.test {
            // Skip initial Loading state
            assertThat(awaitItem()).isEqualTo(InventoryUiState.Loading)

            val category1 = Category(id = 1L, name = "Category 1")
            val category2 = Category(id = 2L, name = "Category 2")

            val item1 = Item(
                id = 10L,
                categoryId = category1.id,
                name = "Tomato",
                icon = null,
                quantity = null,
                unit = null,
                expirationDate = null,
                storageConditions = null
            )
            val item2 = Item(
                id = 20L,
                categoryId = category2.id,
                name = "Apple",
                icon = null,
                quantity = null,
                unit = null,
                expirationDate = null,
                storageConditions = null
            )

            val testInventory = listOf(
                CategoryInventory(category = category1, items = listOf(item1)),
                CategoryInventory(category = category2, items = listOf(item2)),
            )

            // Setup initial data
            inventoryRepository.sendInventory(testInventory)
            val initialState = awaitItem() as InventoryUiState.Success

            assertThat(initialState.selectedCategory).isNull()
            assertThat(initialState.inventory).hasSize(2)

            // Action: Select 'Category 1'
            viewModel.onCategoryFilterSelected(category1)

            // Verify: List is filtered
            val filteredState = awaitItem() as InventoryUiState.Success
            assertThat(filteredState.selectedCategory).isEqualTo(category1)
            assertThat(filteredState.inventory).hasSize(1)
            assertThat(filteredState.inventory[0].category.id).isEqualTo(1L)

            // Action: Click 'Category 1' again to toggle off
            viewModel.onCategoryFilterSelected(category1)

            // Verify: Filter is cleared
            val toggledState = awaitItem() as InventoryUiState.Success
            assertThat(toggledState.selectedCategory).isNull()
            assertThat(toggledState.inventory).hasSize(2)

            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * Verifies that the [InventoryViewModel.onItemAdd] method rejects invalid inputs
     * (empty name, zero categoryId) and properly formats and saves valid inputs.
     */
    @Test
    fun onItemAddIgnoresInvalidInputAndSavesValidInput() = runTest {
        // Attempt to save item with blank name
        viewModel.onItemAdd(
            name = "   ",
            categoryId = 1L,
            quantityStr = "2.5",
            unit = "kg"
        )
        assertThat(inventoryRepository.savedItems).isEmpty()

        // Attempt to save item with invalid category ID (0L)
        viewModel.onItemAdd(
            name = "Carrot",
            categoryId = 0L,
            quantityStr = "1",
            unit = "kg"
        )
        assertThat(inventoryRepository.savedItems).isEmpty()

        // Attempt to save valid item
        viewModel.onItemAdd(
            name = " Carrot ", // Testing trim() functionality
            categoryId = 1L,
            quantityStr = "2.5",
            unit = "kg"
        )

        // Verify valid item was saved successfully
        assertThat(inventoryRepository.savedItems).hasSize(1)

        val savedItem = inventoryRepository.savedItems.first()
        assertThat(savedItem.name).isEqualTo("Морковь") // Should be trimmed
        assertThat(savedItem.categoryId).isEqualTo(1L)
        assertThat(savedItem.quantity).isEqualTo(2.5)
        assertThat(savedItem.unit).isEqualTo("кг")
    }

    /**
     * Verifies that if a user inputs an invalid quantity string (e.g., text instead of numbers)
     * or a blank unit, they are safely converted to null before saving to the database.
     */
    @Test
    fun onItemAddConvertsInvalidQuantityToNull() = runTest {
        viewModel.onItemAdd(
            name = "Test Item",
            categoryId = 1L,
            quantityStr = "not a number", // Invalid double
            unit = "    " // Blank unit
        )

        val savedItem = inventoryRepository.savedItems.first()
        assertThat(savedItem.quantity).isNull()
        assertThat(savedItem.unit).isNull()
    }
}
