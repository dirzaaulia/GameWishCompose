package com.dirzaaulia.gamewish.ui.home.deals

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.paging.compose.LazyPagingItems
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.data.model.cheapshark.Deals
import com.dirzaaulia.gamewish.data.model.rawg.Stores
import com.dirzaaulia.gamewish.data.request.cheapshark.DealsRequest
import com.dirzaaulia.gamewish.ui.common.CommonVerticalList
import com.dirzaaulia.gamewish.ui.common.ErrorConnect
import com.dirzaaulia.gamewish.ui.common.item.DealItem
import com.dirzaaulia.gamewish.ui.home.HomeViewModel
import com.dirzaaulia.gamewish.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun Deals(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
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
                modifier = modifier,
                containerColor = MaterialTheme.colorScheme.surface,
                scaffoldState = scaffoldState,
                topBar = {
                    dealsRequest.storeID?.let {
                        stores?.get(it.toInt() - 1)?.storeName?.let { storeName ->
                            DealsAppBar(
                                storeName = storeName,
                                scope = scope,
                                scaffoldState = scaffoldState
                            )
                        }
                    }
                },
                sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                sheetPeekHeight = 0.dp,
                sheetContent = {
                    DealsFilter(
                        viewModel = viewModel,
                        stores = stores
                    )
                },
            ) {
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
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
    stores: List<Stores>?,
) {
    var storeIndex by rememberSaveable { mutableStateOf(OtherConstant.ONE) }
    var storeQuery by rememberSaveable { mutableStateOf(CheapSharkConstant.CHEAPSHARK_DEFAULT_STORE_NAME) }
    var storeExpanded by remember { mutableStateOf(false) }
    var storeFieldSize by remember { mutableStateOf(Size.Zero) }
    var lowPrice by rememberSaveable { mutableStateOf(CheapSharkConstant.CHEAPSHARK_DEFAULT_LOWER_PRICE) }
    var upperPrice by rememberSaveable { mutableStateOf(CheapSharkConstant.CHEAPSHARK_DEFAULT_UPPER_PRICE) }
    var titleQuery by rememberSaveable { mutableStateOf(OtherConstant.EMPTY_STRING) }
    val aaaState = remember { mutableStateOf(false) }
    val icon = if (storeExpanded)
        Icons.Filled.ArrowDropUp
    else
        Icons.Filled.ArrowDropDown

    Column(
        modifier = modifier.padding(8.dp)
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
            textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
            label = {
                Text(
                    text = CheapSharkConstant.CHEAPSHARK_STORE,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = OtherConstant.EMPTY_STRING,
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
                    text = { Text(text = item.storeName.toString()) },
                    onClick = {
                        storeIndex = index + OtherConstant.ONE
                        storeQuery = item.storeName.toString()
                        storeExpanded = false
                        viewModel.setDealsRequest(
                            DealsRequest(
                                (index + OtherConstant.ONE).toString(),
                                lowPrice,
                                upperPrice,
                                titleQuery,
                                aaaState.value
                            )
                        )
                    }
                )
            }
        }
        OutlinedTextField(
            value = lowPrice.toString(),
            onValueChange = { value ->
                lowPrice = if (value.isEmpty()) {
                    0
                } else {
                    value.filter { it.isDigit() }.toLong()
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
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
            leadingIcon = { Text(text = stringResource(R.string.dollar_sign)) },
            label = {
                Text(
                    text = stringResource(R.string.minimum_price),
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = upperPrice.toString(),
            onValueChange = { value ->
                upperPrice = if (value.isEmpty()) {
                    0
                } else {
                    value.filter { it.isDigit() }.toLong()
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
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
            leadingIcon = { Text(text = stringResource(id = R.string.dollar_sign)) },
            label = {
                Text(
                    text = stringResource(R.string.maximum_price),
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
            label = {
                Text(
                    text = stringResource(R.string.game_title),
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 4.dp),
        ) {
            Text(
                text = stringResource(R.string.aaa_games),
                style = MaterialTheme.typography.bodyMedium
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
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        CommonVerticalList(
            data = data,
            lazyListState = lazyListState,
            placeholderType = PlaceholderConstant.DEALS,
            emptyString = stringResource(R.string.deals_empty),
            errorString = stringResource(id = R.string.deals_error),
        ) { deals ->
            DealItem(deals = deals)
        }
    }
}

@Composable
fun DealsAppBar(
    storeName: String,
    scope: CoroutineScope,
    scaffoldState: BottomSheetScaffoldState
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = storeName,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = {
                    scope.launch {
                        scaffoldState.bottomSheetState.expand()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.FilterList,
                    contentDescription = OtherConstant.EMPTY_STRING,
                )
            }
        }
    }
}
