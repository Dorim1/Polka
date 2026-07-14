package ru.anlyashenko.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.anlyashenko.core.database.dao.CategoryDAO
import ru.anlyashenko.core.database.dao.ItemDAO
import ru.anlyashenko.core.database.entity.CategoryDBO
import ru.anlyashenko.core.database.entity.ItemDBO

@Database(
    entities = [CategoryDBO::class, ItemDBO::class],
    version = 1,
    exportSchema = true
)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDAO
    abstract fun itemDao(): ItemDAO
}
