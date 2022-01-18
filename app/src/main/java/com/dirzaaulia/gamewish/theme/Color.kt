package com.dirzaaulia.gamewish.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver

val Black = Color(0xFF000000)
val Black800 = Color(0xFF121212)
val BlackAlpha20 = Color(0x33000000)
val BlackAlpha87 = Color(0xDE000000)
val BlueGrey200 = Color(0xFFB0BEC5)
val BlueGrey300 = Color(0xFF90A4AE)
val BlueGrey400 = Color(0xFF78909C)
val LightBlue700 = Color(0xFF0288D1)
val Green700 = Color(0xFF388E3C)
val Grey50 = Color(0xFFFAFAFA)
val Grey400 = Color(0xFFBDBDBD)
val Grey500 = Color(0xFF9E9E9E)
val Grey700 = Color(0xFF616161)
val Grey800 = Color(0xFF424242)
val GreyAlpha60 = Color(0x99FAFAFA)
val Red200 = Color(0xFFEF9A9A)
val Red400 = Color(0xFFEF5350)
val Red700 = Color(0xFFD32F2F)
val White = Color(0xFFFFFFFF)
val WhiteAlpha60 = Color(0x99FFFFFF)


val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

/**
 * Return the fully opaque color that results from compositing [onSurface] atop [surface] with the
 * given [alpha]. Useful for situations where semi-transparent colors are undesirable.
 */
@Composable
fun Colors.compositedOnSurface(alpha: Float): Color {
    return onSurface.copy(alpha = alpha).compositeOver(surface)
}