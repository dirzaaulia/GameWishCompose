package com.dirzaaulia.gamewish.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.dp

private val LightElevation = Elevations()

private val DarkElevation = Elevations(card = 1.dp)

private val DarkColorPalette = darkColors(
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

private val LightColorPalette = lightColors(
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
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val elevation = if (darkTheme) DarkElevation else LightElevation

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

/**
 * Alternate to [MaterialTheme] allowing us to add our own theme systems (e.g. [Elevations]) or to
 * extend [MaterialTheme]'s types e.g. return our own [Colors] extension
 */
object GameWishTheme {

    /**
     * Proxy to [MaterialTheme]
     */
    val colors: Colors
        @Composable
        get() = MaterialTheme.colors

    /**
     * Proxy to [MaterialTheme]
     */
    val typography: Typography
        @Composable
        get() = MaterialTheme.typography

    /**
     * Proxy to [MaterialTheme]
     */
    val shapes: Shapes
        @Composable
        get() = MaterialTheme.shapes

    /**
     * Retrieves the current [Elevations] at the call site's position in the hierarchy.
     */
    val elevations: Elevations
        @Composable
        get() = LocalElevations.current

    /**
     * Retrieves the current [Images] at the call site's position in the hierarchy.
     */
    val images: Images
        @Composable
        get() = LocalImages.current
}
