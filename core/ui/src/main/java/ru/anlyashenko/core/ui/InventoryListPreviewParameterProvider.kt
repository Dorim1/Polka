package ru.anlyashenko.core.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.anlyashenko.core.model.CategoryInventory

class InventoryListPreviewParameterProvider : PreviewParameterProvider<List<CategoryInventory>> {
    override val values: Sequence<List<CategoryInventory>> = sequenceOf(
        PreviewParameterData.inventoryList
    )
}

