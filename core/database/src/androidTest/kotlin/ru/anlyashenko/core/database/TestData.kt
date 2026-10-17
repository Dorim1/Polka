package ru.anlyashenko.core.database

import ru.anlyashenko.core.database.entity.CategoryDBO
import ru.anlyashenko.core.database.entity.ItemDBO

internal fun testCategory(id: Long = 0, name: String) = CategoryDBO(id = id, name = name)

internal fun testItem(
    id: Long = 0,
    categoryId: Long,
    name: String,
    quantity: Double? = null,
) = ItemDBO(
    id = id,
    categoryId = categoryId,
    name = name,
    icon = null,
    quantity = quantity,
    unit = null,
    expirationDate = null,
    storageConditions = null,
    createdAt = 0L,
    updatedAt = 0L,
)
