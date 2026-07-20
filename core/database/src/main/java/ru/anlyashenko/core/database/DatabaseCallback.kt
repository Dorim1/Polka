package ru.anlyashenko.core.database

import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteConnection
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.anlyashenko.core.common.di.IoDispatcher
import ru.anlyashenko.core.database.dao.CategoryDAO
import ru.anlyashenko.core.database.dao.ItemDAO
import ru.anlyashenko.core.database.entity.CategoryDBO
import ru.anlyashenko.core.database.entity.ItemDBO
import javax.inject.Inject
import javax.inject.Provider

internal class DatabaseCallback @Inject constructor(
    private val databaseProvider: Provider<AppDatabase>,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : RoomDatabase.Callback() {

    override fun onCreate(connection: SQLiteConnection) {
        super.onCreate(connection)

        CoroutineScope(ioDispatcher).launch {
            val database = databaseProvider.get()
            val categoryDAO = database.categoryDao()
            val itemDAO = database.itemDao()

            populateInitialData(categoryDAO, itemDAO)
        }
    }
}

private suspend fun populateInitialData(categoryDao: CategoryDAO, itemDao: ItemDAO) {
    val now = System.currentTimeMillis()

    val vsyakoeId = categoryDao.insertCategory(CategoryDBO(name = "Всякое"))
    val molochkaId = categoryDao.insertCategory(CategoryDBO(name = "Молочка"))
    val saucesId = categoryDao.insertCategory(CategoryDBO(name = "Соусы"))
    categoryDao.insertCategory(CategoryDBO(name = "Ягоды"))
    categoryDao.insertCategory(CategoryDBO(name = "Крупы"))
    categoryDao.insertCategory(CategoryDBO(name = "Рыба и мясо"))

    itemDao.insertItem(
        ItemDBO(
            categoryId = vsyakoeId,
            name = "Мюсли",
            icon = "🥥",
            quantity = 1.0,
            unit = "пачка",
            expirationDate = null,
            storageConditions = null,
            createdAt = now,
            updatedAt = now
        )
    )
    itemDao.insertItem(
        ItemDBO(
            categoryId = vsyakoeId,
            name = "Пыльца",
            icon = "🌾",
            quantity = 100.0,
            unit = "г",
            expirationDate = null,
            storageConditions = null,
            createdAt = now,
            updatedAt = now
        )
    )
    itemDao.insertItem(
        ItemDBO(
            categoryId = vsyakoeId,
            name = "Мёд",
            icon = "🍯",
            quantity = null,
            unit = "много",
            expirationDate = null,
            storageConditions = null,
            createdAt = now,
            updatedAt = now
        )
    )
    itemDao.insertItem(
        ItemDBO(
            categoryId = vsyakoeId,
            name = "Голубика",
            icon = "🫐",
            quantity = 170.0,
            unit = "г",
            expirationDate = null,
            storageConditions = null,
            createdAt = now,
            updatedAt = now
        )
    )

    itemDao.insertItem(
        ItemDBO(
            categoryId = molochkaId,
            name = "Сметана",
            icon = "🍶",
            quantity = 2.0,
            unit = "банки",
            expirationDate = null,
            storageConditions = null,
            createdAt = now,
            updatedAt = now
        )
    )
    itemDao.insertItem(
        ItemDBO(
            categoryId = molochkaId,
            name = "Масло",
            icon = "🧈",
            quantity = 170.0,
            unit = "г",
            expirationDate = null,
            storageConditions = null,
            createdAt = now,
            updatedAt = now
        )
    )
    itemDao.insertItem(
        ItemDBO(
            categoryId = molochkaId,
            name = "Сыр",
            icon = "🧀",
            quantity = 260.0,
            unit = "г",
            expirationDate = null,
            storageConditions = null,
            createdAt = now,
            updatedAt = now
        )
    )
    itemDao.insertItem(
        ItemDBO(
            categoryId = molochkaId,
            name = "Яйца",
            icon = "🥚",
            quantity = 7.0,
            unit = "шт",
            expirationDate = null,
            storageConditions = null,
            needsAttention = true, // Включаем "!"
            createdAt = now,
            updatedAt = now
        )
    )

    itemDao.insertItem(
        ItemDBO(
            categoryId = saucesId,
            name = "Аджика",
            icon = "🌶️",
            quantity = 100.0,
            unit = "г",
            expirationDate = null,
            storageConditions = null,
            createdAt = now,
            updatedAt = now
        )
    )
    itemDao.insertItem(
        ItemDBO(
            categoryId = saucesId,
            name = "Кисло-сладкий соус из мака",
            icon = "🫙",
            quantity = 100.0,
            unit = "г",
            expirationDate = null,
            storageConditions = null,
            createdAt = now,
            updatedAt = now
        )
    )
    itemDao.insertItem(
        ItemDBO(
            categoryId = saucesId,
            name = "Горчица",
            icon = "🫙",
            quantity = 100.0,
            unit = "г",
            expirationDate = null,
            storageConditions = null,
            createdAt = now,
            updatedAt = now
        )
    )
}

