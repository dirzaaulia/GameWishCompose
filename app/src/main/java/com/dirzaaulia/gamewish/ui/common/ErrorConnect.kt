package com.dirzaaulia.gamewish.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SignalWifiBad
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.theme.GameWishTheme

@Composable
fun ErrorConnect(
    text: String,
    repeat: () -> Unit,
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        val (constraintLayout) = createRefs()
        ConstraintLayout(
            modifier = Modifier
                .background(Color.Transparent)
                .constrainAs(constraintLayout) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.SignalWifiBad,
                    contentDescription = "Signal Wifi Bad",
                    tint = MaterialTheme.colors.onSurface,
                    modifier = Modifier.size(100.dp)
                )
                Text(
                    text = text,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .padding(30.dp)
                )
                OutlinedButton(
                    onClick = repeat,
                ) {
                    Text(text = stringResource(id = R.string.no_connection_button))
                }

            }
        }
    }
}

@Preview
@Composable
fun ErrorConnectPreviewLight() {
    GameWishTheme(darkTheme = false) {
        ErrorConnect(stringResource(id = R.string.no_connection)) {}
    }
}

@Preview
@Composable
fun ErrorConnectPreviewDark() {
    GameWishTheme(darkTheme = true) {
        ErrorConnect(stringResource(id = R.string.no_connection)) {}
    }
}