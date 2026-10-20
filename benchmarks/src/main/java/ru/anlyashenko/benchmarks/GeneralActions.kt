package ru.anlyashenko.benchmarks

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import androidx.test.uiautomator.util.Patterns
import java.util.regex.Pattern

fun MacrobenchmarkScope.inventoryWaitForContent() {
    device.wait(Until.gone(By.res("loading_indicator")), 5_000)
    device.waitAndFindObject(By.res("inventory:list"), 10_000)
}

fun MacrobenchmarkScope.inventoryScrollDownUp() {
    val list = device.findObject(By.res("inventory:list"))
    device.flingElementDownUp(list)
}

fun MacrobenchmarkScope.startOnInventory() {
    startActivityAndWait()
    if (device.hasObject(By.res("aura:screen"))) {
        device.pressBack()
    }
    inventoryWaitForContent()
}

fun MacrobenchmarkScope.goToAuraScreen() {
    device.waitAndFindObject(By.desc("Aura"), 5_000).click()
    device.waitAndFindObject(By.res("aura:screen"), 5_000)
}

fun MacrobenchmarkScope.inventoryToggleCategories(times: Int = 6) {
    val chipSelector = By.res(Pattern.compile("category_filter_\\d+"))
    repeat(times) { i ->
        val chips = device.findObjects(chipSelector)
        check(chips.size >= 2) {
            "Need at least 2 category chips on iteration $i, found ${chips.size}\n" +
                    device.dumpWindowHierarchy()
        }
        chips[i % 2].click()
        device.waitForIdle()
    }
}
