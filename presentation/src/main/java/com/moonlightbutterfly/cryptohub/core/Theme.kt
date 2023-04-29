package com.moonlightbutterfly.cryptohub.core

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.moonlightbutterfly.cryptohub.core.Color.red
import com.moonlightbutterfly.cryptohub.core.Color.redDark
import com.moonlightbutterfly.cryptohub.core.Color.yellow
import com.moonlightbutterfly.cryptohub.core.Color.yellowDark

private val DarkColorPalette = darkColors(
    primary = yellowDark,
    primaryVariant = yellowDark,
    onPrimary = Color.White,
    secondary = redDark,
    secondaryVariant = redDark,
)

private val LightColorPalette = lightColors(
    primary = yellow,
    primaryVariant = yellow,
    onPrimary = Color.Black,
    secondary = red,
    secondaryVariant = red,
)

@Composable
fun CryptoHubTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}
