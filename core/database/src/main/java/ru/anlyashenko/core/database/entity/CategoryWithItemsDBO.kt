package ru.anlyashenko.core.database.entity

import androidx.room.Embedded
import androidx.room.Relation
import ru.anlyashenko.core.model.CategoryInventory

data class CategoryWithItemsDBO(
    @Embedded
    val categoryDBO: CategoryDBO,
    @Relation(
        parentColumn = "id",
        entityColumn = "category_id"
    )
    val items: List<ItemDBO>
)

fun CategoryWithItemsDBO.asExternalModel() = CategoryInventory(
    category = categoryDBO.asExternalModel(),
    items = items.map(ItemDBO::asExternalModel)
)
