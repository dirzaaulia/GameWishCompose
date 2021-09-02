package com.dirzaaulia.gamewish.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.dirzaaulia.gamewish.ui.theme.White

@Composable
fun GameFilterDialog(
    searchQuery : String,
    openDialog : Boolean,
    dismissDialog : () -> Unit
) {
    var text by rememberSaveable { mutableStateOf(searchQuery) }

    if (openDialog) {
        AlertDialog(
            shape = MaterialTheme.shapes.large,
            onDismissRequest = {
                dismissDialog()
            },
            title = { Text(
                text = "Filter List",
                style = MaterialTheme.typography.h6
            ) },
            text = {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        textStyle = TextStyle(color = MaterialTheme.colors.onSurface),
                        label = {
                            Text(
                                text = "Game Name",
                                color = MaterialTheme.colors.onSurface
                            )
                        },
                        placeholder = {
                            if (text.isEmpty()) {
                                Text(
                                    text = "Game Name",
                                    color = MaterialTheme.colors.onSurface
                                )
                            } else {
                                Text(
                                    text = text,
                                    color = MaterialTheme.colors.onSurface
                                )
                            }
                        }
                    )
                }
            },
            dismissButton = {
                Button(onClick = { dismissDialog() }) {
                    Text("Dismiss")
                }
            },
            confirmButton = {
                Button(onClick = { dismissDialog() }) {
                    Text("Filter")
                }
            }
        )
    }
}