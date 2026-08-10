package ru.anlyashenko.feature.aura.impl

import org.intellij.lang.annotations.Language

@Language("AGSL")
const val AURA_SHADER = """
    uniform float2 resolution;
    layout(color) uniform half4 color1;
    layout(color) uniform half4 color2;
    layout(color) uniform half4 color3;
    layout(color) uniform half4 color4;
    uniform float seed;
    uniform float time;

    vec4 main(in vec2 fragCoord) {
        vec2 uv = fragCoord.xy / resolution.xy;
        vec2 cUv = uv * 2.0 - 1.0;
        
        // Корректируем пропорции, чтобы сфера всегда была круглой
        float aspect = resolution.x / resolution.y;
        cUv.x *= aspect;
        
        float dist = length(cUv);

        // =========================================================
        // 1. ЧЁТКОСТЬ И ФОРМА КРАЁВ (КАК НА РЕФЕРЕНСЕ)
        // =========================================================
        
        // Плотное ядро. Делаем спад шире (от 0.65 до 0.45), 
        // чтобы край был читаемым, но хорошо "замыленным", как на фото.
        float core = smoothstep(0.65, 0.45, dist);
        
        // Широкий мягкий ореол по гауссовой кривой.
        // Функция exp(-x^2) дает идеальный, бесконечно растворяющийся свет.
        // Число 2.5 контролирует ширину свечения (меньше = шире).
        float glow = exp(-dist * dist * 5.0) * 0.7;
        
        // Итоговая маска прозрачности
        float alpha = clamp(core + glow, 0.0, 1.0);

        // =========================================================
        // 2. ОРГАНИЧНЫЕ ПЯТНА-МЕТАБОЛЫ (ЦВЕТ)
        // =========================================================
        
        // Искажаем координаты для создания эффекта "жидкости".
        // Это заставляет пятна не просто двигаться, а плавно деформироваться.
        vec2 warp = cUv;
        warp.x += sin(cUv.y * 2.5 + time * 0.3 + seed) * 0.4;
        warp.y += cos(cUv.x * 2.5 - time * 0.4 - seed) * 0.4;

        // Генерируем очень мягкие волны на основе искаженных координат.
        // Мы НЕ используем здесь smoothstep, чтобы сохранить максимальную
        // плавность градиентов без видимых границ.
        float w1 = sin(warp.x * 6.0 + time * 0.5) * 0.5 + 0.5; // 7.0 -> 2.0
        float w2 = cos(warp.y * 6.0 - time * 0.3) * 0.5 + 0.5; // 7.0 -> 2.0

        // Смешивание 4-х цветов
        half4 mix1 = mix(color1, color2, w1);
        half4 mix2 = mix(color3, color4, w2);
        
        // Финальное диагональное смешивание тоже делаем мягким
        // и привязываем к "жидким" координатам для объема.
        float fluidPattern = sin((warp.x + warp.y) * 4.5) * 0.5 + 0.5; // 5.5 -> 1.5
        half4 finalColor = mix(mix1, mix2, fluidPattern);

        // Возвращаем итоговый цвет с учетом прозрачности 
        // (pre-multiplied alpha для правильного наложения)
        return half4(finalColor.rgb * alpha, alpha);
    }
"""
