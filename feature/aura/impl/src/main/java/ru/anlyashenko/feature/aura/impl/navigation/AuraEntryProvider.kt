package ru.anlyashenko.feature.aura.impl.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ru.anlyashenko.core.navigation.Navigator
import ru.anlyashenko.feature.aura.api.AuraNavKey
import ru.anlyashenko.feature.aura.impl.AuraGeneratorScreen

fun EntryProviderScope<NavKey>.auraEntry(navigator: Navigator) {
    entry<AuraNavKey> {
        AuraGeneratorScreen()
    }
}
