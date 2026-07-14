package ru.anlyashenko.core.database.entity

import androidx.room.Embedded
import androidx.room.Relation


data class CategoryWithItemsDBO(
    @Embedded
    val categoryDBO: CategoryDBO,
    @Relation(
        parentColumn = "id",
        entityColumn = "category_id"
    )
    val items: List<ItemDBO>
)
