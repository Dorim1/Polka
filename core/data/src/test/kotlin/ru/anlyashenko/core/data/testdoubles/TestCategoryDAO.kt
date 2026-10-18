package ru.anlyashenko.core.data.testdoubles

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.anlyashenko.core.database.dao.CategoryDAO
import ru.anlyashenko.core.database.entity.CategoryDBO
import ru.anlyashenko.core.database.entity.CategoryWithItemsDBO

/**
 * Fake implementation of [CategoryDAO] for testing.
 */
class TestCategoryDAO : CategoryDAO {
    private val categoriesWithItemsFlow = MutableStateFlow<List<CategoryWithItemsDBO>>(emptyList())

    override fun getCategoriesWithItems(): Flow<List<CategoryWithItemsDBO>> {
        return categoriesWithItemsFlow
    }

    /**
     * Test-only API to emit a new state to the [categoriesWithItemsFlow].
     */
    fun sendCategoriesWithItems(data: List<CategoryWithItemsDBO>) {
        categoriesWithItemsFlow.value = data
    }

    override fun getAllCategories(): Flow<List<CategoryDBO>> = MutableStateFlow(emptyList())
    override suspend fun insertCategory(category: CategoryDBO): Long = 0L
    override suspend fun deleteCategory(category: CategoryDBO) {  }

}
