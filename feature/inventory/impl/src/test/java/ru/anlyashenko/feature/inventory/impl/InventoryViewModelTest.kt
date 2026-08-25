package ru.anlyashenko.feature.inventory.impl

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Before
import org.junit.Rule
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
}
