package ru.anlyashenko.feature.inventory.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ru.anlyashenko.core.navigation.Navigator
import ru.anlyashenko.feature.aura.api.navigateToAura
import ru.anlyashenko.feature.inventory.api.InventoryNavKey
import ru.anlyashenko.feature.inventory.impl.InventoryScreen

fun EntryProviderScope<NavKey>.inventoryEntry(navigator: Navigator) {
    entry<InventoryNavKey> {
        InventoryScreen(
            onBackClick = { navigator.goBack() },
            onAuraClick = navigator::navigateToAura
        )
    }
}
