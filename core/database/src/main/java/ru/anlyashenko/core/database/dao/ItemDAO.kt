package ru.anlyashenko.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.anlyashenko.core.database.entity.ItemDBO

@Dao
interface ItemDAO {

    @Query("SELECT * FROM items WHERE category_id = :categoryId")
    fun getItemsByCategory(categoryId: Long): Flow<List<ItemDBO>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ItemDBO): Long

    @Update
    suspend fun updateItem(item: ItemDBO)

    @Query("UPDATE items SET needs_attention = :needsAttention WHERE id = :itemId")
    suspend fun updateAttentionFlag(itemId: Long, needsAttention: Boolean)

    @Delete
    suspend fun deleteItem(item: ItemDBO)
}
