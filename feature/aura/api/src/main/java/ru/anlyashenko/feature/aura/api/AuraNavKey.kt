package ru.anlyashenko.feature.aura.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import ru.anlyashenko.core.navigation.Navigator

@Serializable
object AuraNavKey : NavKey {
}

fun Navigator.navigateToAura() {
    navigate(AuraNavKey)
}
