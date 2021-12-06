package com.dirzaaulia.gamewish.ui.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dirzaaulia.gamewish.data.model.rawg.Platforms
import com.dirzaaulia.gamewish.data.model.rawg.Stores
import com.dirzaaulia.gamewish.ui.theme.White
import com.dirzaaulia.gamewish.utils.setPlatformsBackgroundColor
import com.google.accompanist.flowlayout.FlowColumn
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
        backgroundColor = setPlatformsBackgroundColor(data, code),
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .padding(4.dp)
    ) {
        data.platform?.name?.let {
            Text(
                text = it,
                color = White,
                style = MaterialTheme.typography.subtitle1,
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
        backgroundColor = setPlatformsBackgroundColor(data, code),
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .padding(4.dp)
    ) {
        data.store?.name?.let {
            Text(
                text = it,
                color = White,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 12.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}