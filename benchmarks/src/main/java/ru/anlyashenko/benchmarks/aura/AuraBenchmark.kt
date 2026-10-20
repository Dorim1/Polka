package ru.anlyashenko.benchmarks.aura

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import perfetto.protos.AndroidCpuMetric
import ru.anlyashenko.benchmarks.PACKAGE_NAME
import ru.anlyashenko.benchmarks.goToAuraScreen
import ru.anlyashenko.benchmarks.inventoryWaitForContent
import ru.anlyashenko.benchmarks.startOnInventory

class AuraBenchmark {
    @get:Rule val rule = MacrobenchmarkRule()

    @Test
    fun auraSteadyState() = rule.measureRepeated(
        packageName = PACKAGE_NAME,
        metrics = listOf(FrameTimingMetric()),
        compilationMode = CompilationMode.Partial(),
        iterations = 5,
        startupMode = StartupMode.WARM,
        setupBlock = {
            pressHome()
            startOnInventory()
            goToAuraScreen()
        },
    ) {
        Thread.sleep(5_000)
    }

    @Test
    fun openAura() = rule.measureRepeated(
        packageName = PACKAGE_NAME,
        metrics = listOf(FrameTimingMetric()),
        compilationMode = CompilationMode.Partial(),
        iterations = 5,
        startupMode = StartupMode.WARM,
        setupBlock = {
            pressHome()
            startOnInventory()
        },
    ) {
        goToAuraScreen()
        Thread.sleep(1_000)
    }
}
