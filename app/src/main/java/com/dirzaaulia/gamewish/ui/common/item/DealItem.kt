package com.dirzaaulia.gamewish.ui.common.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.dirzaaulia.gamewish.data.model.cheapshark.Deals
import com.dirzaaulia.gamewish.utils.CheapSharkConstant
import com.dirzaaulia.gamewish.utils.NetworkImage
import com.dirzaaulia.gamewish.utils.openDeals
import com.dirzaaulia.gamewish.utils.toCurrencyFormat

@Composable
fun DealItem(
    modifier: Modifier = Modifier,
    deals: Deals,
) {
    val context = LocalContext.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 4.dp)
            .clickable(
                onClick = { deals.dealID?.let { openDeals(context = context, it) } }
            ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            NetworkImage(
                url = deals.thumb,
                modifier = modifier
                    .height(150.dp)
                    .width(100.dp)
                    .fillMaxHeight(),
                contentScale = ContentScale.FillBounds
            )
            Column(
                modifier = modifier
                    .padding(4.dp)
                    .weight(1f)
            ) {
                Surface(
                    modifier = Modifier.padding(top = 4.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                ) {
                    Text(
                        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                        textAlign = TextAlign.Center,
                        text = String.format(
                            CheapSharkConstant.CHEAPSHARK_DEAL_FORMAT,
                            deals.savings?.toFloat()
                        ),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = deals.title.toString(),
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = deals.normalPrice?.toDouble().toCurrencyFormat(),
                    style = MaterialTheme.typography.labelSmall,
                    textDecoration = TextDecoration.LineThrough
                )
                Text(
                    text = deals.salePrice?.toDouble().toCurrencyFormat(),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}