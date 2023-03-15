package com.dirzaaulia.gamewish.ui.home.wishlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.ui.home.HomeViewModel
import com.dirzaaulia.gamewish.utils.MyAnimeListConstant
import com.dirzaaulia.gamewish.utils.OtherConstant
import com.dirzaaulia.gamewish.utils.RawgConstant
import com.dirzaaulia.gamewish.utils.TmdbConstant
import com.dirzaaulia.gamewish.utils.lowerCaseWords

@Composable
fun GameFilterDialog(
    viewModel: HomeViewModel,
    gameStatus: String,
    searchQuery: String
) {

    var query by rememberSaveable { mutableStateOf(searchQuery) }
    val statusList = listOf(
        OtherConstant.ALL,
        RawgConstant.RAWG_STATUS_PLAYING,
        RawgConstant.RAWG_STATUS_PLAYING,
        RawgConstant.RAWG_STATUS_ON_HOLD,
        RawgConstant.RAWG_STATUS_DROPPED,
        RawgConstant.RAWG_STATUS_PLAN_TO_BUY
    )
    var status by rememberSaveable { mutableStateOf(gameStatus) }
    var textfieldSize by remember { mutableStateOf(Size.Zero) }
    var expanded by remember { mutableStateOf(false) }
    val icon = if (expanded)
        Icons.Filled.ArrowDropUp
    else
        Icons.Filled.ArrowDropDown

    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .imePadding()
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.filter_game),
            style = MaterialTheme.typography.h6
        )
        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                viewModel.setGameQuery(query)
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.game_name)) },
            placeholder = {
                if (query.isBlank()) {
                    Text(text = stringResource(R.string.game_name))
                } else {
                    Text(text = query)
                }
            }
        )
        Text(
            text = stringResource(R.string.sort_game),
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

                        var listStatus = status

                        if (listStatus.equals(OtherConstant.ALL, true)) {
                            listStatus = OtherConstant.EMPTY_STRING
                        }

                        viewModel.setGameStatus(listStatus)
                    }
                ) {
                    Text(text = item)
                }
            }
        }
    }
}

@Composable
fun AnimeFilterDialog(
    viewModel: HomeViewModel,
    animeStatus: String
) {

    val statusList = listOf(
        OtherConstant.ALL,
        MyAnimeListConstant.MYANIMELIST_STATUS_WATCHING,
        MyAnimeListConstant.MYANIMELIST_STATUS_COMPLETED,
        MyAnimeListConstant.MYANIMELIST_STATUS_ON_HOLD,
        MyAnimeListConstant.MYANIMELIST_STATUS_DROPPED,
        MyAnimeListConstant.MYANIMELIST_STATUS_PLAN_TO_WATCH
    )
    var status by rememberSaveable { mutableStateOf(animeStatus) }
    var textfieldSize by remember { mutableStateOf(Size.Zero) }
    var expanded by remember { mutableStateOf(false) }
    val icon = if (expanded)
        Icons.Filled.ArrowDropUp
    else
        Icons.Filled.ArrowDropDown

    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .imePadding()
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.sort_anime),
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
            label = { Text(text = stringResource(id = R.string.status)) },
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

                        var listStatus = status
                            .lowerCaseWords()
                            .replace(OtherConstant.BLANK_SPACE, OtherConstant.UNDERSCORE)

                        if (listStatus.equals(OtherConstant.ALL, true)) {
                            listStatus = OtherConstant.BLANK_SPACE
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

@Composable
fun MangaFilterDialog(
    viewModel: HomeViewModel,
    mangaStatus: String
) {

    val statusList = listOf(
        OtherConstant.ALL,
        MyAnimeListConstant.MYANIMELIST_STATUS_READING,
        MyAnimeListConstant.MYANIMELIST_STATUS_COMPLETED,
        MyAnimeListConstant.MYANIMELIST_STATUS_ON_HOLD,
        MyAnimeListConstant.MYANIMELIST_STATUS_DROPPED,
        MyAnimeListConstant.MYANIMELIST_STATUS_PLAN_TO_READ
    )
    var status by rememberSaveable { mutableStateOf(mangaStatus) }
    var textfieldSize by remember { mutableStateOf(Size.Zero) }
    var expanded by remember { mutableStateOf(false) }
    val icon = if (expanded)
        Icons.Filled.ArrowDropUp
    else
        Icons.Filled.ArrowDropDown

    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .imePadding()
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.sort_manga),
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
            label = { Text(text = stringResource(id = R.string.status)) },
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
            modifier = Modifier.width(with(LocalDensity.current) { textfieldSize.width.toDp() })
        ) {
            statusList.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        status = item
                        expanded = false

                        var listStatus = status
                            .lowerCaseWords()
                            .replace(OtherConstant.BLANK_SPACE, OtherConstant.UNDERSCORE)

                        if (listStatus.equals(OtherConstant.ALL, true)) {
                            listStatus = OtherConstant.EMPTY_STRING
                        }

                        viewModel.setMangaStatus(listStatus)
                    }
                ) {
                    Text(text = item)
                }
            }
        }
    }
}

@Composable
fun MovieFilterDialog(
    viewModel: HomeViewModel,
    movieStatus: String,
    searchQuery: String,
    type: String
) {

    var query by rememberSaveable { mutableStateOf(searchQuery) }
    val statusList = listOf(
        OtherConstant.ALL,
        TmdbConstant.TMDB_STATUS_WATCHING,
        TmdbConstant.TMDB_STATUS_COMPLETED,
        TmdbConstant.TMDB_STATUS_ON_HOLD,
        TmdbConstant.TMDB_STATUS_DROPPED,
        TmdbConstant.TMDB_STATUS_PLAN_TO_WATCH
    )
    var status by rememberSaveable { mutableStateOf(movieStatus) }
    var textfieldSize by remember { mutableStateOf(Size.Zero) }
    var expanded by remember { mutableStateOf(false) }
    val icon = if (expanded)
        Icons.Filled.ArrowDropUp
    else
        Icons.Filled.ArrowDropDown

    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .imePadding()
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.filter_movie),
            style = MaterialTheme.typography.h6
        )
        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it

                if (type.equals(TmdbConstant.TMDB_TYPE_MOVIE, true)) {
                    viewModel.setMovieQuery(query)
                } else {
                    viewModel.setTVShowQuery(query)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(R.string.movie_name)) },
            placeholder = {
                if (query.isBlank()) {
                    Text(text = stringResource(R.string.movie_name))
                } else {
                    Text(text = query)
                }
            }
        )
        Text(
            text = stringResource(R.string.sort_movie),
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
            label = { Text("Status") },
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
            modifier = Modifier.width(with(LocalDensity.current) { textfieldSize.width.toDp() })
        ) {
            statusList.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        status = item
                        expanded = false

                        var listStatus = status

                        if (listStatus.equals(OtherConstant.ALL, true)) {
                            listStatus = OtherConstant.EMPTY_STRING
                        }

                        if (type.equals(TmdbConstant.TMDB_TYPE_MOVIE, true)) {
                            viewModel.setMovieStatus(listStatus)
                        } else {
                            viewModel.setTVShowStatus(listStatus)
                        }
                    }
                ) {
                    Text(text = item)
                }
            }
        }
    }
}