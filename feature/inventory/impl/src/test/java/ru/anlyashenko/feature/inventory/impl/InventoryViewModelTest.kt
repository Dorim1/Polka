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
            // todo: SavedStateHandle()
        )
    }

    @Test
    fun stateIsInitiallyLoading() = runTest {
        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(InventoryUiState.Loading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun stateIsSuccess_whenInventoryLoads() = runTest {
        viewModel.uiState.test {
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

            inventoryRepository.sendInventory(testCategories)

            val successState = awaitItem() as InventoryUiState.Success

            assertThat(successState.inventory).isEqualTo(testCategories)
            assertThat(successState.filterCategories).containsExactly(sampleCategory)
            assertThat(successState.selectedCategory).isNull()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun categoryFilter_togglesAndFiltersInventory() = runTest {
        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(InventoryUiState.Loading)

            val category1 = Category(id = 1L, name = "Category 1")
            val category2 = Category(id = 2L, name = "Category 2")

            val item1 = Item(
                id = 10L,
                categoryId = category1.id,
                name = "Помидор",
                icon = null,
                quantity = null,
                unit = null,
                expirationDate = null,
                storageConditions = null
            )
            val item2 = Item(
                id = 20L,
                categoryId = category2.id,
                name = "Яблоко",
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

            inventoryRepository.sendInventory(testInventory)
            val initialState = awaitItem() as InventoryUiState.Success

            assertThat(initialState.selectedCategory).isNull()
            assertThat(initialState.inventory).hasSize(2)

            viewModel.onCategoryFilterSelected(category1)

            val filteredState = awaitItem() as InventoryUiState.Success
            assertThat(filteredState.selectedCategory).isEqualTo(category1)
            assertThat(filteredState.inventory).hasSize(1)
            assertThat(filteredState.inventory[0].category.id).isEqualTo(1L)

            viewModel.onCategoryFilterSelected(category1)

            val toggledState = awaitItem() as InventoryUiState.Success
            assertThat(toggledState.selectedCategory).isNull()
            assertThat(toggledState.inventory).hasSize(2)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun onItemAdd_ignoresInvalidInput_andSavesValidInput() = runTest {
        viewModel.onItemAdd(
            name = "   ",
            categoryId = 1L,
            quantityStr = "2.5",
            unit = "кг"
        )
        // репозиторий ничего не сохранил
        assertThat(inventoryRepository.savedItems).isEmpty()

        // 2. предмет с categoryId == 0L
        viewModel.onItemAdd(
            name = "Морковь",
            categoryId = 0L,
            quantityStr = "1",
            unit = "кг"
        )
        assertThat(inventoryRepository.savedItems).isEmpty()

        // 3. ПРАВИЛЬНЫЙ предмет
        viewModel.onItemAdd(
            name = " Морковь ", // проверить trim()
            categoryId = 1L,
            quantityStr = "2.5",
            unit = "кг"
        )

        // Ждем, пока отработает корутина во ViewModel (viewModelScope.launch)
        // В тестах runTest это происходит мгновенно

        // предмет сохранился
        assertThat(inventoryRepository.savedItems).hasSize(1)

        val savedItem = inventoryRepository.savedItems.first()
        assertThat(savedItem.name).isEqualTo("Морковь") // Проверили trim()
        assertThat(savedItem.categoryId).isEqualTo(1L)
        assertThat(savedItem.quantity).isEqualTo(2.5)
        assertThat(savedItem.unit).isEqualTo("кг")
    }
}
