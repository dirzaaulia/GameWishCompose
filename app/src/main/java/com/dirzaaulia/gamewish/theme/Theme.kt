package com.dirzaaulia.gamewish.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.dp

private val lightElevation = Elevations()

private val darkElevation = Elevations(card = 1.dp)

private val darkColorPalette = darkColors(
    primary = Grey400,
    primaryVariant = Grey500,
    secondary = BlueGrey400,
    secondaryVariant = BlueGrey300,
    background = Black,
    surface = Black800,
    error = Red700,
    onPrimary = Black,
    onSecondary = Black,
    onBackground = Grey50,
    onSurface = Grey50,
    onError = Black
)

private val lightColorPalette = lightColors(
    primary = Grey700,
    primaryVariant = Grey800,
    secondary = BlueGrey300,
    secondaryVariant = BlueGrey200,
    background = Grey50,
    surface = White,
    error = Red700,
    onPrimary = White,
    onSecondary = Black,
    onBackground = Black,
    onSurface = Black,
    onError = Black
)

@Composable
fun GameWishTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColorPalette
    } else {
        lightColorPalette
    }

    val elevation = if (darkTheme) darkElevation else lightElevation

    CompositionLocalProvider(
        LocalElevations provides elevation,
    ) {
        MaterialTheme(
            colors = colors,
            typography = typography,
            shapes = shapes,
            content = content
        )
    }
}