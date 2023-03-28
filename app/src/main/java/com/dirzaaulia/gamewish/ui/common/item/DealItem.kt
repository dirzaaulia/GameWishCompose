package com.dirzaaulia.gamewish.ui.common.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
            modifier = Modifier.height(150.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NetworkImage(
                url = deals.thumb,
                modifier = modifier
                    .width(100.dp)
                    .fillMaxHeight(),
            )
            Column(
                modifier = modifier
                    .padding(start = 8.dp)
                    .weight(1f)
            ) {
                AssistChip(
                    onClick = { },
                    label = {
                        Text(
                            text = String.format(
                                CheapSharkConstant.CHEAPSHARK_DEAL_FORMAT,
                                deals.savings?.toFloat()
                            ),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                )
                Text(
                    text = deals.title.toString(),
                    style = MaterialTheme.typography.titleLarge
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