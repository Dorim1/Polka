package ru.anlyashenko.core.data.model

import ru.anlyashenko.core.database.entity.ItemDBO
import ru.anlyashenko.core.model.Item

fun Item.asEntity(
    createdAt: Long = System.currentTimeMillis(),
    updatedAt: Long = System.currentTimeMillis()
) = ItemDBO(
    id = id,
    categoryId = categoryId,
    name = name,
    icon = icon,
    quantity = quantity,
    unit = unit,
    expirationDate = expirationDate,
    storageConditions = storageConditions,
    needsAttention = needsAttention,
    createdAt = createdAt,
    updatedAt = updatedAt
)
