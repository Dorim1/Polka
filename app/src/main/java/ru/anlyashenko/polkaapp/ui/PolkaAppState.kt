package ru.anlyashenko.polkaapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavKey
import ru.anlyashenko.core.navigation.NavigationState
import ru.anlyashenko.core.navigation.rememberNavigationState

@Composable
fun rememberTestAppState(
    startKey: NavKey,
): PolkaAppState {
    val topLevelKeys = remember(startKey) {
        setOf(startKey)
    }

    val navigationState = rememberNavigationState(startKey, topLevelKeys)

    return remember(navigationState) {
        PolkaAppState(navigationState)
    }
}

@Stable
class PolkaAppState(
    val navigationState: NavigationState
) {
}
