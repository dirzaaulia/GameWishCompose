package com.dirzaaulia.gamewish.ui.home.deals

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.paging.compose.LazyPagingItems
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.data.model.cheapshark.Deals
import com.dirzaaulia.gamewish.data.model.rawg.Stores
import com.dirzaaulia.gamewish.data.request.cheapshark.DealsRequest
import com.dirzaaulia.gamewish.extension.isError
import com.dirzaaulia.gamewish.extension.isSucceeded
import com.dirzaaulia.gamewish.ui.common.CommonVerticalList
import com.dirzaaulia.gamewish.ui.common.DealsItem
import com.dirzaaulia.gamewish.ui.common.ErrorConnect
import com.dirzaaulia.gamewish.ui.home.HomeViewModel
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun Deals(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState,
    data: LazyPagingItems<Deals>
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val dealsRequest by viewModel.dealsRequest.collectAsState()
    val storesResult by viewModel.storesResult.collectAsState()
    val stores by viewModel.stores.collectAsState()

    when {
        storesResult.isSucceeded -> {
            BottomSheetScaffold(
                scaffoldState = scaffoldState,
                topBar = { DealsAppBar(scope, scaffoldState) },
                sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                sheetPeekHeight = 0.dp,
                sheetContent = {
                    DealsFilter(viewModel, stores)
                },
                backgroundColor = MaterialTheme.colors.background,
                modifier = modifier
            ) {
                dealsRequest.storeID?.let {
                    stores?.get(it.toInt() - 1)?.storeName?.let { storeName ->
                        Text(
                            text = storeName,
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }
                DealsList(
                    data = data,
                    lazyListState = lazyListState,
                )
            }
        }
    }

    when {
        storesResult.isError -> {
            ErrorConnect(text = stringResource(id = R.string.stores_error)) {
                viewModel.getStores()
                data.retry()
            }
        }
    }
}

@Composable
fun DealsFilter(
    viewModel: HomeViewModel,
    stores: List<Stores>?,
) {
    var storeIndex by rememberSaveable { mutableStateOf(1) }
    var storeQuery by rememberSaveable { mutableStateOf("Steam") }
    var storeExpanded by remember { mutableStateOf(false) }
    var storeFieldSize by remember { mutableStateOf(Size.Zero) }
    var lowPrice by rememberSaveable { mutableStateOf<Long>(0) }
    var upperPrice by rememberSaveable { mutableStateOf<Long>(1000) }
    var titleQuery by rememberSaveable { mutableStateOf("") }
    val aaaState = remember { mutableStateOf(false) }
    val icon = if (storeExpanded)
        Icons.Filled.ArrowDropUp //it requires androidx.compose.material:material-icons-extended
    else
        Icons.Filled.ArrowDropDown

    Column(
        modifier = Modifier
            .navigationBarsWithImePadding()
            .padding(vertical = 8.dp, horizontal = 8.dp)
    ) {
        OutlinedTextField(
            value = storeQuery,
            onValueChange = { storeQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    storeFieldSize = coordinates.size.toSize()
                },
            textStyle = TextStyle(color = MaterialTheme.colors.onSurface),
            label = {
                Text(
                    text = "Store",
                    color = MaterialTheme.colors.onSurface
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.clickable { storeExpanded = !storeExpanded }
                )
            },
            readOnly = true
        )
        DropdownMenu(
            expanded = storeExpanded,
            onDismissRequest = { storeExpanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { storeFieldSize.width.toDp() })
        ) {
            stores?.forEachIndexed { index, item ->
                DropdownMenuItem(
                    onClick = {
                        storeIndex = index + 1
                        storeQuery = item.storeName.toString()
                        storeExpanded = false
                        viewModel.setDealsRequest(
                            DealsRequest(
                                (index + 1).toString(),
                                lowPrice,
                                upperPrice,
                                titleQuery,
                                aaaState.value
                            )
                        )
                    }
                ) {
                    Text(text = item.storeName.toString())
                }
            }
        }
        OutlinedTextField(
            value = lowPrice.toString(),
            onValueChange = {
                lowPrice = if (it.isEmpty()) {
                    0
                } else {
                    it.toLong()
                }

                viewModel.setDealsRequest(
                    DealsRequest(
                        storeIndex.toString(),
                        lowPrice,
                        upperPrice,
                        titleQuery,
                        aaaState.value
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
            textStyle = TextStyle(color = MaterialTheme.colors.onSurface),
            leadingIcon = { Text(text = "$") },
            label = {
                Text(
                    text = "Minimum Price",
                    color = MaterialTheme.colors.onSurface
                )
            },
        )
        OutlinedTextField(
            value = upperPrice.toString(),
            onValueChange = {
                upperPrice = if (it.isEmpty()) {
                    0
                } else {
                    it.toLong()
                }

                viewModel.setDealsRequest(
                    DealsRequest(
                        storeIndex.toString(),
                        lowPrice,
                        upperPrice,
                        titleQuery,
                        aaaState.value
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
            textStyle = TextStyle(color = MaterialTheme.colors.onSurface),
            leadingIcon = { Text(text = "$") },
            label = {
                Text(
                    text = "Maximum Price",
                    color = MaterialTheme.colors.onSurface
                )
            },
        )
        OutlinedTextField(
            value = titleQuery,
            onValueChange = {
                titleQuery = it

                viewModel.setDealsRequest(
                    DealsRequest(
                        storeIndex.toString(),
                        lowPrice,
                        upperPrice,
                        titleQuery,
                        aaaState.value
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
            textStyle = TextStyle(color = MaterialTheme.colors.onSurface),
            label = {
                Text(
                    text = "Game Title",
                    color = MaterialTheme.colors.onSurface
                )
            },
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 4.dp),
        ) {
            Text(
                text = "AAA Games",
                style = MaterialTheme.typography.subtitle1
            )
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.End
            ) {
                Switch(
                    checked = aaaState.value,
                    onCheckedChange = {
                        aaaState.value = it
                        viewModel.setDealsRequest(
                            DealsRequest(
                                storeIndex.toString(),
                                lowPrice,
                                upperPrice,
                                titleQuery,
                                aaaState.value
                            )
                        )
                    },
                )
            }
        }
    }
}

@Composable
fun DealsList(
    data: LazyPagingItems<Deals>,
    lazyListState: LazyListState,
) {
    Box(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        CommonVerticalList(
            data = data,
            lazyListState = lazyListState,
            emptyString = "There is no Deals found! Try again with different search parameters using filter button on top right",
            errorString = stringResource(id = R.string.deals_error),
        ) { deals ->
            DealsItem(deals = deals)
        }
    }
}

@Composable
fun DealsAppBar(
    scope: CoroutineScope,
    scaffoldState: BottomSheetScaffoldState
) {
    TopAppBar(
        elevation = 0.dp,
        modifier = Modifier
            .height(80.dp)
            .statusBarsPadding()
    ) {
        Image(
            modifier = Modifier
                .padding(0.dp, 16.dp, 0.dp, 16.dp)
                .size(100.dp, 0.dp)
                .align(Alignment.CenterVertically)
                .aspectRatio(1.0f),
            painter = painterResource(id = R.drawable.ic_gamewish_dark),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            IconButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = {
                    scope.launch {
                        if (scaffoldState.bottomSheetState.isCollapsed) {
                            scaffoldState.bottomSheetState.expand()
                        } else {
                            scaffoldState.bottomSheetState.collapse()
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.FilterList,
                    contentDescription = null,
                )
            }
        }
    }
}
