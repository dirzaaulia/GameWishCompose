package com.dirzaaulia.gamewish.ui.common.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.dirzaaulia.gamewish.data.model.cheapshark.Deals
import com.dirzaaulia.gamewish.utils.NetworkImage
import com.dirzaaulia.gamewish.utils.OtherConstant
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
            .height(150.dp)
            .padding(vertical = 4.dp)
            .clickable(
                onClick = { deals.dealID?.let { openDeals(context = context, it) } }
            ),
        shape = MaterialTheme.shapes.large,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            val url = deals.thumb?.ifBlank {
                OtherConstant.NO_IMAGE_URL
            }

            NetworkImage(
                url = url.toString(),
                contentDescription = null,
                modifier = modifier
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
                    color = MaterialTheme.colors.primary,
                ) {
                    Text(
                        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                        textAlign = TextAlign.Center,
                        text = String.format("%.2f%% Off", deals.savings?.toFloat()),
                        style = MaterialTheme.typography.caption
                    )
                }
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = deals.title.toString(),
                    style = MaterialTheme.typography.subtitle1
                )
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = deals.normalPrice?.toDouble().toCurrencyFormat(),
                    style = MaterialTheme.typography.caption,
                    textDecoration = TextDecoration.LineThrough
                )
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = deals.salePrice?.toDouble().toCurrencyFormat(),
                    style = MaterialTheme.typography.caption
                )
            }
        }
    }
}