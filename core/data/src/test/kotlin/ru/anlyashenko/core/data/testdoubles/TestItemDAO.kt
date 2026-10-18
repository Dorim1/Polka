package ru.anlyashenko.core.data.testdoubles

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.anlyashenko.core.database.dao.ItemDAO
import ru.anlyashenko.core.database.entity.ItemDBO

/**
 * Fake implementation of [ItemDAO] for testing.
 */
class TestItemDAO : ItemDAO {
    val savedItems = mutableListOf<ItemDBO>()

    override suspend fun insertItem(item: ItemDBO): Long {
        savedItems.add(item)
        return item.id
    }

    override fun getItemsByCategory(categoryId: Long): Flow<List<ItemDBO>> = MutableStateFlow(emptyList())
    override suspend fun updateItem(item: ItemDBO) { }
    override suspend fun updateAttentionFlag(itemId: Long, needsAttention: Boolean) { }
    override suspend fun deleteItem(item: ItemDBO) { }
}
