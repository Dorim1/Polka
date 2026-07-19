package ru.anlyashenko.feature.inventory.impl

import ru.anlyashenko.core.model.Category
import ru.anlyashenko.core.model.CategoryInventory

sealed interface InventoryUiState {

    data object Loading: InventoryUiState

    data class Success(
        val inventory: List<CategoryInventory>,
        val filterCategories: List<Category>,
        val selectedCategory: Category? = null,
    ) : InventoryUiState

}
