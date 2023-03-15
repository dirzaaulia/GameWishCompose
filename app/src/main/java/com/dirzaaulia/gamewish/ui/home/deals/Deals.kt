package com.dirzaaulia.gamewish.ui.home.deals

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.primarySurface
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.dirzaaulia.gamewish.utils.isError
import com.dirzaaulia.gamewish.utils.isSucceeded
import com.dirzaaulia.gamewish.ui.common.CommonVerticalList
import com.dirzaaulia.gamewish.ui.common.ErrorConnect
import com.dirzaaulia.gamewish.ui.common.item.DealItem
import com.dirzaaulia.gamewish.ui.home.HomeViewModel
import com.dirzaaulia.gamewish.utils.CheapSharkConstant
import com.dirzaaulia.gamewish.utils.OtherConstant
import com.dirzaaulia.gamewish.utils.PlaceholderConstant
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
                modifier = modifier,
                backgroundColor = MaterialTheme.colors.primarySurface,
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
            textStyle = TextStyle(color = MaterialTheme.colors.onSurface),
            label = {
                Text(
                    text = CheapSharkConstant.CHEAPSHARK_STORE,
                    color = MaterialTheme.colors.onSurface
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
                ) {
                    Text(text = item.storeName.toString())
                }
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
            textStyle = TextStyle(color = MaterialTheme.colors.onSurface),
            leadingIcon = { Text(text = stringResource(R.string.dollar_sign)) },
            label = {
                Text(
                    text = stringResource(R.string.minimum_price),
                    color = MaterialTheme.colors.onSurface
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
            textStyle = TextStyle(color = MaterialTheme.colors.onSurface),
            leadingIcon = { Text(text = stringResource(id = R.string.dollar_sign)) },
            label = {
                Text(
                    text = stringResource(R.string.maximum_price),
                    color = MaterialTheme.colors.onSurface
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
            textStyle = TextStyle(color = MaterialTheme.colors.onSurface),
            label = {
                Text(
                    text = stringResource(R.string.game_title),
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
                text = stringResource(R.string.aaa_games),
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
        modifier = Modifier
            .background(MaterialTheme.colors.background)
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
    TopAppBar(
        elevation = 0.dp,
        modifier = Modifier
            .statusBarsPadding()
            .wrapContentHeight()
    ) {
        Text(
            text = storeName,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(horizontal = 8.dp)
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
                    contentDescription = OtherConstant.EMPTY_STRING,
                )
            }
        }
    }
}
