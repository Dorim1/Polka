package ru.anlyashenko.feature.inventory.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.anlyashenko.core.data.repository.InventoryRepository
import ru.anlyashenko.core.model.Category
import ru.anlyashenko.core.model.Item
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val inventoryRepository: InventoryRepository
) : ViewModel() {
    private val selectedCategory = MutableStateFlow<Category?>(null)

    val uiState: StateFlow<InventoryUiState> = combine(
        inventoryRepository.getInventory(),
        selectedCategory
    ) { inventory, selected ->
        val categories = inventory.map { it.category }

        val filteredInventory = if (selected != null) {
            inventory.filter { it.category.id == selected.id }
        } else {
            inventory
        }

        InventoryUiState.Success(
            inventory = filteredInventory,
            filterCategories = categories,
            selectedCategory = selected
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = InventoryUiState.Loading
        )

    fun onCategoryFilterSelected(category: Category) {
        if (selectedCategory.value?.id == category.id) {
            selectedCategory.value = null
        } else {
            selectedCategory.value = category
        }
    }

    fun onItemClicked(item: Item) {
        // todo
    }

    fun onItemAdd(name: String, categoryId: Long, quantityStr: String, unit: String) {
        if (name.isBlank() || categoryId == 0L) return

        viewModelScope.launch {
            val quantity = quantityStr.toDoubleOrNull()

            val newItem = Item(
                categoryId = categoryId,
                name = name.trim(),
                icon = null,
                quantity = quantity,
                unit = unit.ifBlank { null },
                expirationDate = null,
                storageConditions = null,
                needsAttention = false
            )

            inventoryRepository.saveItem(newItem)
        }
    }

    fun onItemDelete(item: Item) {
        // todo
    }

}
