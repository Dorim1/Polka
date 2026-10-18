package ru.anlyashenko.core.testing.repository

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.anlyashenko.core.data.repository.InventoryRepository
import ru.anlyashenko.core.model.CategoryInventory
import ru.anlyashenko.core.model.Item

class TestInventoryRepository : InventoryRepository {

    /**
     * The backing hot flow for the list of inventory ids for testing.
     */
    private val inventoryFlow : MutableSharedFlow<List<CategoryInventory>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    val savedItems = mutableListOf<Item>()

    override fun getInventory(): Flow<List<CategoryInventory>> = inventoryFlow

    override suspend fun saveItem(item: Item) {
        savedItems.add(item)
    }

    /**
     * A test-only API to allow controlling the list of news resources from tests.
     */
    fun sendInventory(inventory: List<CategoryInventory>) {
        inventoryFlow.tryEmit(inventory)
    }
}
