package com.dirzaaulia.gamewish.ui.home.wishlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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

@Composable
fun FilterDialog(
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
        modifier = Modifier
            .navigationBarsPadding()
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
                    style = MaterialTheme.typography.h6
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
            text = type.setSortTitle(),
            style = MaterialTheme.typography.h6
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
                    contentDescription = null,
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
                ) {
                    Text(text = item)
                }
            }
        }
    }
}