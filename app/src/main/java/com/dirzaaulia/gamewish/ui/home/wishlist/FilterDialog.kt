package com.dirzaaulia.gamewish.ui.home.wishlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.data.model.wishlist.FilterDialogType
import com.dirzaaulia.gamewish.data.model.wishlist.FilterDialogType.Companion.doSort
import com.dirzaaulia.gamewish.data.model.wishlist.FilterDialogType.Companion.getFilterList
import com.dirzaaulia.gamewish.data.model.wishlist.FilterDialogType.Companion.setFilterName
import com.dirzaaulia.gamewish.data.model.wishlist.FilterDialogType.Companion.setFilterTitle
import com.dirzaaulia.gamewish.data.model.wishlist.FilterDialogType.Companion.setSortTitle
import com.dirzaaulia.gamewish.data.model.wishlist.FilterDialogType.Companion.setStatus
import com.dirzaaulia.gamewish.ui.home.HomeViewModel
import com.dirzaaulia.gamewish.utils.OtherConstant
import com.google.accompanist.insets.navigationBarsHeight

@Composable
fun FilterDialog(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
    filterStatus: String,
    searchQuery: String = OtherConstant.EMPTY_STRING,
    type: FilterDialogType
) {
    var query by rememberSaveable {
        mutableStateOf(searchQuery)
    }
    var status by rememberSaveable {
        mutableStateOf(type.setStatus(filterStatus))
    }
    var textfieldSize by remember { mutableStateOf(Size.Zero) }
    var expanded by remember { mutableStateOf(false) }

    val icon = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown
    val statusList = type.getFilterList()

    Column(
        modifier = modifier
            .imePadding()
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        when (type) {
            FilterDialogType.GAME,
            FilterDialogType.MOVIE,
            FilterDialogType.TV -> {
                Text(
                    text = type.setFilterTitle(),
                    style = MaterialTheme.typography.titleMedium
                )
                OutlinedTextField(
                    value = query,
                    onValueChange = {
                        query = it
                        when (type) {
                            FilterDialogType.GAME -> viewModel.setGameQuery(query)
                            FilterDialogType.MOVIE -> viewModel.setMovieQuery(query)
                            FilterDialogType.TV -> viewModel.setTVShowQuery(query)
                            else -> Unit
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = type.setFilterName()) },
                    placeholder = {
                        if (query.isBlank()) Text(text = type.setFilterName()) else Text(text = query)
                    }
                )
            }
            else -> { }
        }

        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = type.setSortTitle(),
            style = MaterialTheme.typography.titleMedium
        )
        OutlinedTextField(
            readOnly = true,
            value = status,
            onValueChange = { status = it },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textfieldSize = coordinates.size.toSize()
                },
            label = { Text(text = stringResource(R.string.status)) },
            trailingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = OtherConstant.EMPTY_STRING,
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            },
            placeholder = { Text(text = status) }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textfieldSize.width.toDp() })
        ) {
            statusList.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = {
                        status = item
                        expanded = false

                        var tempStatus = status

                        if (tempStatus.equals(OtherConstant.ALL, true)) {
                            tempStatus = OtherConstant.EMPTY_STRING
                        }

                        type.doSort(
                            viewModel = viewModel,
                            status = tempStatus
                        )
                    }
                )
            }
        }
    }
}