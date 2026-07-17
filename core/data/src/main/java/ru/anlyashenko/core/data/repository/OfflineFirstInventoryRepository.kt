package ru.anlyashenko.core.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.anlyashenko.core.common.di.IoDispatcher
import ru.anlyashenko.core.data.model.asEntity
import ru.anlyashenko.core.database.dao.CategoryDAO
import ru.anlyashenko.core.database.dao.ItemDAO
import ru.anlyashenko.core.database.entity.CategoryDBO
import ru.anlyashenko.core.database.entity.asExternalModel
import ru.anlyashenko.core.model.CategoryInventory
import ru.anlyashenko.core.model.Item
import javax.inject.Inject

internal class OfflineFirstInventoryRepository @Inject constructor(
    private val categoryDAO: CategoryDAO,
    private val itemDAO: ItemDAO,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
): InventoryRepository {
    override fun getInventory(): Flow<List<CategoryInventory>> {
        return categoryDAO.getCategoriesWithItems()
            .map { listDBO ->
                listDBO.map { it.asExternalModel() }
            }
            .flowOn(ioDispatcher)
    }

    override suspend fun saveItem(item: Item) {
        withContext(ioDispatcher) {
            itemDAO.insertItem(item.asEntity())
        }
    }

}
