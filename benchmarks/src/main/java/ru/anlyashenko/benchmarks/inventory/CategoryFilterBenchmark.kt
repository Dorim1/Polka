package ru.anlyashenko.benchmarks.inventory

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.anlyashenko.benchmarks.PACKAGE_NAME
import ru.anlyashenko.benchmarks.inventoryToggleCategories
import ru.anlyashenko.benchmarks.inventoryWaitForContent

@RunWith(AndroidJUnit4::class)
class CategoryFilterBenchmark {
    @get:Rule val rule = MacrobenchmarkRule()

    @Test
    fun toggleCategories() = rule.measureRepeated(
        packageName = PACKAGE_NAME,
        metrics = listOf(FrameTimingMetric()),
        compilationMode = CompilationMode.Partial(),
        iterations = 10,
        startupMode = StartupMode.WARM,
        setupBlock = {
            pressHome()
            startActivityAndWait()
            inventoryWaitForContent()
        },
    ) {
        inventoryToggleCategories(times = 6)
    }

}
