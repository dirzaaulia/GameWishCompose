package com.dirzaaulia.gamewish.ui.common.list

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dirzaaulia.gamewish.data.model.rawg.Platforms
import com.dirzaaulia.gamewish.data.model.rawg.Stores
import com.dirzaaulia.gamewish.utils.setPlatformsBackgroundColor
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun GameDetailsPlatformList(
    data: List<Platforms>,
    code: Int
) {
    FlowRow {
        data
            .sortedWith(compareBy { it.platform?.name?.length })
            .forEach {
            GameDetailsItemPlatforms(data = it, code = code)
        }
    }
}

@Composable
fun GameDetailsItemPlatforms(
    data: Platforms,
    code: Int
) {
    Card(
        modifier = Modifier.padding(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = setPlatformsBackgroundColor(data, code)
        ),
    ) {
        data.platform?.name?.let {
            Text(
                text = it,
                color = White,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 12.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun GameDetailsStoresList(
    data: List<Stores>,
    code: Int
) {
    FlowRow {
        data
            .sortedWith(compareBy { it.store?.name?.length })
            .forEach {
                GameDetailsItemStores(data = it, code = code)
            }
    }
}

@Composable
fun GameDetailsItemStores(
    data: Stores,
    code: Int
) {
    Card(
        modifier = Modifier.padding(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = setPlatformsBackgroundColor(data, code)
        ),
    ) {
        data.store?.name?.let {
            Text(
                text = it,
                color = White,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 12.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}