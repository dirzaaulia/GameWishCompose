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

@Composable
fun GameFilterDialog(
    viewModel: HomeViewModel,
    searchQuery: String,
    openDialog: Boolean,
    dismissDialog: () -> Unit
) {
    var query by rememberSaveable { mutableStateOf(searchQuery) }

    if (openDialog) {
        AlertDialog(
            shape = MaterialTheme.shapes.large,
            onDismissRequest = {
                dismissDialog()
            },
            title = {
                Text(
                    text = "Filter List",
                    style = MaterialTheme.typography.h6
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
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
                            if (query.isEmpty()) {
                                Text(
                                    text = "Game Name",
                                    color = MaterialTheme.colors.onSurface
                                )
                            } else {
                                Text(
                                    text = query,
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
                Button(onClick = {
                    viewModel.setSearchQuery(query)
                    dismissDialog()
                }) {
                    Text("Filter")
                }
            }
        )
    }
}