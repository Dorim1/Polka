package ru.anlyashenko.core.data.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import ru.anlyashenko.core.data.testdoubles.TestCategoryDAO
import ru.anlyashenko.core.data.testdoubles.TestItemDAO
import ru.anlyashenko.core.database.entity.CategoryDBO
import ru.anlyashenko.core.database.entity.CategoryWithItemsDBO
import ru.anlyashenko.core.database.entity.ItemDBO
import ru.anlyashenko.core.model.Item

@OptIn(ExperimentalCoroutinesApi::class)
class OfflineFirstInventoryRepositoryTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var categoryDAO: TestCategoryDAO
    private lateinit var itemDAO: TestItemDAO

    private lateinit var subject: OfflineFirstInventoryRepository

    @Before
    fun setup() {
        categoryDAO = TestCategoryDAO()
        itemDAO = TestItemDAO()

        subject = OfflineFirstInventoryRepository(
            categoryDAO = categoryDAO,
            itemDAO = itemDAO,
            ioDispatcher = testDispatcher
        )
    }

    /**
     * Verifies that [OfflineFirstInventoryRepository.getInventory] reads from the DAO
     * and maps the database models (DBO) to external UI models correctly.
     */
    @Test
    fun getInventory_isBackedByCategoryDaoAndMappedToExternalModel() = runTest(testDispatcher) {
        // Prepare fake DBO data
        val fakeDboData = listOf(
            CategoryWithItemsDBO(
                categoryDBO = CategoryDBO(id = 1L, name = "Dairy"),
                items = listOf(
                    ItemDBO(
                        id = 10L, categoryId = 1L, name = "Milk", icon = null,
                        quantity = 1.0, unit = "L", expirationDate = null,
                        storageConditions = null, needsAttention = false,
                        createdAt = 0L, updatedAt = 0L
                    )
                )
            )
        )

        // load data into the fake DAO
        categoryDAO.sendCategoriesWithItems(fakeDboData)

        // collect the flow from the Repository using Turbine
        subject.getInventory().test {
            val inventory = awaitItem()

            // assert that the data was mapped to External Models correctly
            assertThat(inventory).hasSize(1)

            val firstCategory = inventory.first()
            assertThat(firstCategory.category.id).isEqualTo(1L)
            assertThat(firstCategory.category.name).isEqualTo("Dairy")

            assertThat(firstCategory.items).hasSize(1)
            assertThat(firstCategory.items.first().name).isEqualTo("Milk")
            assertThat(firstCategory.items.first().unit).isEqualTo("L")

            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * Verifies that [OfflineFirstInventoryRepository.saveItem] properly maps
     * the external [Item] model to an [ItemDBO] and inserts it using the [ItemDAO].
     */
    @Test
    fun saveItem_mapsToEntityAndInsertsIntoDao() = runTest(testDispatcher) {
        // create a clean external model
        val itemToSave = Item(
            id = 5L,
            categoryId = 2L,
            name = "Apple",
            icon = null,
            quantity = 3.0,
            unit = "kg",
            expirationDate = null,
            storageConditions = null,
            needsAttention = true
        )

        // call the repository method
        subject.saveItem(itemToSave)

        // verify that the fake DAO received the mapped entity
        val savedItems = itemDAO.savedItems
        assertThat(savedItems).hasSize(1)

        val savedDbo = savedItems.first()
        assertThat(savedDbo.id).isEqualTo(5L)
        assertThat(savedDbo.name).isEqualTo("Apple")
        assertThat(savedDbo.needsAttention).isTrue()

        // Ensure that mapping handled createdAt/updatedAt
        // (Assuming your asEntity mapper sets them to System.currentTimeMillis, they should be > 0)
        assertThat(savedDbo.createdAt).isGreaterThan(0L)
        assertThat(savedDbo.updatedAt).isGreaterThan(0L)
    }
}
