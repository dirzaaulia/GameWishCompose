package com.dirzaaulia.gamewish.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.dirzaaulia.gamewish.extension.visible

@Composable
fun CommonLoading(
    visibility: Boolean
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .visible(visibility)
    ) {
        val (loading) = createRefs()
        CircularProgressIndicator(
            modifier = Modifier
                .constrainAs(loading) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
    }
}

@Composable
fun CommonLoadingItem() {
    CircularProgressIndicator(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}

@Composable
fun CommonLoadingGridItem() {
    CircularProgressIndicator(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .wrapContentSize(Alignment.Center)
    )
}