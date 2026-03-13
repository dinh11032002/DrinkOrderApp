package com.truongdinh.drinkorder.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    // Brand / Primary (CTA)
    primary = GoldPrimary,
    onPrimary = TextOnGold,

    primaryContainer = GoldContainer,
    onPrimaryContainer = TextOnGold,

    // Secondary (ít dùng)
    secondary = GoldPrimary,
    onSecondary = TextOnGold,

    // Background & Surface
    background = BlackBackground,
    onBackground = TextPrimary,

    surface = BlackSurface,
    onSurface = TextPrimary,

    surfaceVariant = BlackSurfaceVariant,
    onSurfaceVariant = TextSecondary,

    // Outline / Divider
    outline = OutlineColor,

    // Status
    error = ErrorRed,
    onError = TextOnGold
)

@Composable
fun DrinkOrderTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}