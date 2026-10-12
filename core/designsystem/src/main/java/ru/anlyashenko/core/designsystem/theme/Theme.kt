package ru.anlyashenko.core.designsystem.theme

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.dp

/**
 * Dark default theme color scheme
 */
@VisibleForTesting
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryColorDark,
    onPrimary = OnPrimaryColorDark,

    secondary = SecondaryColorDark,
    onSecondary = OnSecondaryColorDark,

    background = BackgroundColorDark,
    onBackground = OnBackgroundColorDark,

    surface = SurfaceColorDark,
    onSurface = OnSurfaceColorDark,
)

/**
 * Light default theme color scheme
 */
@VisibleForTesting
private val LightColorScheme = lightColorScheme(
    primary = PrimaryColorLight,
    onPrimary = OnPrimaryColorLight,

    secondary = SecondaryColorLight,
    onSecondary = OnSecondaryColorLight,

    background = BackgroundColorLight,
    onBackground = OnBackgroundColorLight,

    surface = SurfaceColorLight,
    onSurface = OnSurfaceColorLight,

)

@Composable
fun PolkaAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
//    Dynamic color is available on Android 12+
//    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val backgroundTheme = BackgroundTheme(
        color = colorScheme.background, // был surface
        tonalElevation = 2.dp
    )

    CompositionLocalProvider(
        LocalBackgroundTheme provides backgroundTheme
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }


}
