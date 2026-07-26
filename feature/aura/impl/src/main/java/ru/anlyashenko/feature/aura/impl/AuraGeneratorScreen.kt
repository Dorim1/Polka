package ru.anlyashenko.feature.aura.impl

import android.graphics.RuntimeShader
import android.os.Build
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun AuraGeneratorScreen(modifier: Modifier = Modifier) {
    fun generateVibrantColor(): Color {
        return Color.hsv(
            hue = Random.nextFloat() * 360f, // Любой оттенок спектра
            saturation = Random.floatInRange(0.8f, 1.0f), // Высокая насыщенность (без серости)
            value = Random.floatInRange(0.85f, 1.0f) // Высокая яркость (без темноты)
        )
    }

    var color1 by remember { mutableStateOf(generateVibrantColor()) }
    var color2 by remember { mutableStateOf(generateVibrantColor()) }
    var color3 by remember { mutableStateOf(generateVibrantColor()) }
    var color4 by remember { mutableStateOf(generateVibrantColor()) }
    var seed by remember { mutableFloatStateOf(Random.nextFloat() * 100f) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            AuraCanvas(
                color1 = color1,
                color2 = color2,
                color3 = color3,
                color4 = color4,
                seed = seed,
                modifier = Modifier.size(360.dp)
            )
        } else {
            Text(
                text = "Ваше устройство не поддерживает AGSL (нужен Android 13+)",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(32.dp)
            )
        }

        Spacer(Modifier.height(48.dp))

        Button(
            onClick = {
                color1 = generateVibrantColor()
                color2 = generateVibrantColor()
                color3 = generateVibrantColor()
                color4 = generateVibrantColor()
                seed = Random.nextFloat() * 100f
            }
        ) {
            Text("Сгенерировать ULR-000")
        }
    }
}

@androidx.annotation.RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AuraCanvas(
    color1: Color,
    color2: Color,
    color3: Color,
    color4: Color,
    seed: Float,
    modifier: Modifier = Modifier
) {
    val shader = remember { RuntimeShader(AURA_SHADER) }
    val infiniteTransition = rememberInfiniteTransition(label = "aura_transition")

    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 20000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse // Или Reverse, чтобы цвета "дышали" туда-сюда
        ),
        label = "time_animation"
    )

    Canvas(modifier = modifier) {
        shader.setFloatUniform("resolution", size.width, size.height)
        shader.setColorUniform("color1", color1.toArgb())
        shader.setColorUniform("color2", color2.toArgb())
        shader.setColorUniform("color3", color3.toArgb())
        shader.setColorUniform("color4", color4.toArgb())
        shader.setFloatUniform("seed", seed)
        shader.setFloatUniform("time", time)

        // Отрисовываем прямоугольник холста, который заполнится шейдером
        drawRect(brush = ShaderBrush(shader))
    }
}

private fun Random.floatInRange(from: Float, to: Float): Float {
    return from + nextFloat() * (to - from)
}
