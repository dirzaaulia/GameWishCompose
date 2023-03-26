package com.dirzaaulia.gamewish.ui.common.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.dirzaaulia.gamewish.data.model.rawg.SearchTab
import com.dirzaaulia.gamewish.data.model.rawg.SearchTab.Companion.toSearchGameRequest
import com.dirzaaulia.gamewish.ui.search.SearchViewModel
import com.dirzaaulia.gamewish.utils.NetworkImage
import com.dirzaaulia.gamewish.utils.OtherConstant
import timber.log.Timber

@Composable
fun SearchGameTabItem(
    modifier: Modifier = Modifier,
    searchTab: SearchTab,
    viewModel: SearchViewModel? = null,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    viewModel?.apply {
                        selectSearchGameTab(OtherConstant.ZERO)
                        setSearchGameRequest(searchTab.toSearchGameRequest(OtherConstant.EMPTY_STRING))
                        Timber.d("HAYDEN TAG | REQUEST : %s", searchTab.toSearchGameRequest(OtherConstant.EMPTY_STRING).toString())
                    }
                }
            ),
        elevation = 0.dp,
    ) {
        Column {
            val url = searchTab.image.ifBlank {
                OtherConstant.NO_IMAGE_URL
            }

            NetworkImage(
                url = url,
                modifier = modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.FillBounds
            )
            Text(
                text = searchTab.name,
                style = MaterialTheme.typography.h5,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 4.dp, bottom = 4.dp)
            )
        }
    }
}