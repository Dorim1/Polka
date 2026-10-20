package ru.anlyashenko.benchmarks.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import org.junit.Rule
import org.junit.Test
import ru.anlyashenko.benchmarks.PACKAGE_NAME
import ru.anlyashenko.benchmarks.goToAuraScreen
import ru.anlyashenko.benchmarks.inventoryScrollDownUp
import ru.anlyashenko.benchmarks.inventoryWaitForContent

class InventoryBaselineProfile {
    @get:Rule val rule = BaselineProfileRule()

    @Test
    fun generate() = rule.collect(PACKAGE_NAME) {
        startActivityAndWait()
        inventoryWaitForContent()
        inventoryScrollDownUp()
        goToAuraScreen()
        device.pressBack()
    }
}
