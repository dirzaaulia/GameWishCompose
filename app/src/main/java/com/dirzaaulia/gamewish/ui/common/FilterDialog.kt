package com.dirzaaulia.gamewish.ui.home.wishlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.dirzaaulia.gamewish.ui.home.HomeViewModel
import com.dirzaaulia.gamewish.utils.lowerCaseWords
import com.google.accompanist.insets.navigationBarsWithImePadding

@Composable
fun GameFilterDialog(
    viewModel: HomeViewModel,
    searchQuery: String
) {
    var query by rememberSaveable { mutableStateOf(searchQuery) }

    Column(
        modifier = Modifier
            .navigationBarsWithImePadding()
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Filter Game",
            style = MaterialTheme.typography.h6
        )
        OutlinedTextField(
            singleLine = true,
            value = query,
            onValueChange = {
                query = it
                viewModel.setSearchQuery(it)
            },
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
}

@Composable
fun AnimeFilterDialog(
    viewModel: HomeViewModel,
    animeStatus: String
) {

    val statusList = listOf("All", "Watching", "Completed", "On Hold", "Dropped", "Plan To Watch")
    var query by rememberSaveable { mutableStateOf(animeStatus) }
    var textfieldSize by remember { mutableStateOf(Size.Zero) }
    var expanded by remember { mutableStateOf(false) }
    val icon = if (expanded)
        Icons.Filled.ArrowDropUp //it requires androidx.compose.material:material-icons-extended
    else
        Icons.Filled.ArrowDropDown

    if (query.isBlank()) {
        query = "All"
    }

    Column(
        modifier = Modifier
            .navigationBarsWithImePadding()
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Sort Anime",
            style = MaterialTheme.typography.h6
        )
        OutlinedTextField(
            readOnly = true,
            value = query,
            onValueChange = { query = it },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textfieldSize = coordinates.size.toSize()
                },
            label = { Text("Status") },
            trailingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            },
            placeholder = {
                if (query.isBlank()) {
                    Text(text = "All")
                } else {
                    Text(text = query)
                }
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textfieldSize.width.toDp() })
        ) {
            statusList.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        query = item
                        expanded = false

                        var listStatus = query
                        listStatus = listStatus.lowerCaseWords()
                        listStatus = listStatus.replace(" ", "_")

                        if (listStatus.equals("All", true)) {
                            listStatus = ""
                        }

                        viewModel.setAnimeStatus(listStatus)
                    }
                ) {
                    Text(text = item)
                }
            }
        }
    }
}