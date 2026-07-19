package ru.anlyashenko.core.data.repository

import kotlinx.coroutines.flow.Flow
import ru.anlyashenko.core.model.CategoryInventory
import ru.anlyashenko.core.model.Item

interface InventoryRepository {
    fun getInventory(): Flow<List<CategoryInventory>>
    suspend fun saveItem(item: Item)
}
