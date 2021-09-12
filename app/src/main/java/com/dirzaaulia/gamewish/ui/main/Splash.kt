package com.dirzaaulia.gamewish.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.dirzaaulia.gamewish.R

@Composable
fun Splash() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.primarySurface
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(R.drawable.ic_gamewish_dark),
                contentDescription = null,
                modifier = Modifier.aspectRatio(1f)
            )
        }
    }
}