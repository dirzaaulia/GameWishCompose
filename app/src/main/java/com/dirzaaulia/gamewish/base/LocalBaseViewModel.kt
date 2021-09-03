package com.dirzaaulia.gamewish.base

import androidx.compose.runtime.staticCompositionLocalOf
import com.dirzaaulia.gamewish.ui.main.MainViewModel

val LocalBaseViewModel =
    staticCompositionLocalOf<MainViewModel> { error("No MainViewModel found!") }
