package ru.anlyashenko.polkaapp.ui

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import ru.anlyashenko.core.navigation.Navigator
import ru.anlyashenko.core.navigation.toEntries
import ru.anlyashenko.feature.aura.impl.navigation.auraEntry
import ru.anlyashenko.feature.inventory.impl.navigation.inventoryEntry

@Composable
fun PolkaApp(
    startKey: NavKey,
    modifier: Modifier = Modifier,
) {
    val appState = rememberTestAppState(startKey)
    val navigator = remember { Navigator(appState.navigationState) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        val entryProvider = entryProvider {
            inventoryEntry(navigator)
            auraEntry(navigator)
        }

        NavDisplay(
            entries = appState.navigationState.toEntries(entryProvider),
            onBack = { navigator.goBack() },
            modifier = Modifier
                .consumeWindowInsets(innerPadding)
                .padding(innerPadding),
        )
    }
}
