package ru.anlyashenko.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.anlyashenko.core.database.entity.CategoryDBO
import ru.anlyashenko.core.database.entity.CategoryWithItemsDBO

@Dao
interface CategoryDAO {
    @Transaction
    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getCategoriesWithItems(): Flow<List<CategoryWithItemsDBO>>

    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getAllCategories(): Flow<List<CategoryDBO>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryDBO): Long

    @Delete
    suspend fun deleteCategory(category: CategoryDBO)

}
