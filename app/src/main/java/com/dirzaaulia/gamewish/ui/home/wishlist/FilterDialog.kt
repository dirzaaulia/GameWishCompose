package com.dirzaaulia.gamewish.ui.home.wishlist

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
import com.dirzaaulia.gamewish.ui.home.HomeViewModel
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