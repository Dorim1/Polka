package ru.anlyashenko.polkaapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation3.runtime.NavKey
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import ru.anlyashenko.feature.inventory.api.InventoryNavKey

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    var startDestination by mutableStateOf<NavKey?>(null)
        private set

    init {
        startDestination = InventoryNavKey
    }
}
