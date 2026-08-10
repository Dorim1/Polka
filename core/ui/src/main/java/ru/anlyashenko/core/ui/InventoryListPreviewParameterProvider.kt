package ru.anlyashenko.core.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.anlyashenko.core.model.Category
import ru.anlyashenko.core.model.CategoryInventory
import ru.anlyashenko.core.model.Item

class InventoryListPreviewParameterProvider : PreviewParameterProvider<List<CategoryInventory>> {
    override val values: Sequence<List<CategoryInventory>> = sequenceOf(
        PreviewParameterData.inventoryList
    )
}

object PreviewParameterData {

    val categories = listOf(
        Category(1, "Молочка"),
        Category(2, "Соусы"),
        Category(3, "Ягоды"),
        Category(4, "Крупы")
    )

    val inventoryList = listOf(
        CategoryInventory(
            category = Category(10, "Всякое"),
            items = listOf(
                Item(1, 10, "Мюсли", "🥥", 1.0, "пачка", expirationDate = null, storageConditions = null),
                Item(2, 10, "Пыльца", "🌾", 100.0, "г", expirationDate = null, storageConditions = null),
                Item(3, 10, "Мёд", "🍯", null, "много", expirationDate = null, storageConditions = null),
                Item(4, 10, "Голубика", "🫐", 170.0, "г", expirationDate = null, storageConditions = null)
            )
        ),
        CategoryInventory(
            category = Category(11, "Молочка"),
            items = listOf(
                Item(5, 11, "Сметана", "🍶", 2.0, "банки", expirationDate = null, storageConditions = null),
                Item(6, 11, "Масло", "🧈", 170.0, "г", expirationDate = null, storageConditions = null),
                Item(7, 11, "Сыр", "🧀", 260.0, "г", expirationDate = null, storageConditions = null),
                Item(8, 11, "Яйца", "🥚", 7.0, "шт", needsAttention = true, expirationDate = null, storageConditions = null)
            )
        )
    )
}
