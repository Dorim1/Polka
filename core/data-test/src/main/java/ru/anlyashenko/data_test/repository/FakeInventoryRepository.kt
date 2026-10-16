package ru.anlyashenko.data_test.repository

import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import ru.anlyashenko.core.data.repository.InventoryRepository
import ru.anlyashenko.core.model.CategoryInventory
import ru.anlyashenko.core.model.Item
import ru.anlyashenko.data_test.PreviewParameterData

@Singleton
class FakeInventoryRepository @Inject constructor() : InventoryRepository {
    private val inventory = MutableStateFlow(PreviewParameterData.inventoryList)

    override fun getInventory(): Flow<List<CategoryInventory>> = inventory

    override suspend fun saveItem(item: Item) {
        inventory.update { current ->
            current.map { categoryInventory ->
                if (categoryInventory.category.id == item.categoryId) {
                    categoryInventory.copy(items = categoryInventory.items + item)
                } else {
                    categoryInventory
                }
            }
        }
    }
}
