package com.dirzaaulia.gamewish.ui.common.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dirzaaulia.gamewish.data.model.rawg.Games
import com.dirzaaulia.gamewish.utils.OtherConstant
import com.dirzaaulia.gamewish.utils.RawgConstant
import com.dirzaaulia.gamewish.utils.changeDateFormat

@Composable
fun SearchGamesItem(
    modifier: Modifier = Modifier,
    navigateToGameDetails: (Long) -> Unit = { },
    games: Games,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = { games.id?.let { navigateToGameDetails(it) } }
            ),
    ) {
        Column(modifier = modifier.padding(top = 4.dp)) {
            Text(
                text = String.format(
                    OtherConstant.STRING_FORMAT_S_SPACE_S_SPACE_S,
                    RawgConstant.RAWG_RELEASE_DATE,
                    OtherConstant.COLON,
                    games.released?.changeDateFormat(OtherConstant.DATE_FORMAT_STRIP_yyyy_MM_dd)
                ),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
            )
            Text(
                text = games.name.toString(),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(start = 8.dp, end = 4.dp, top = 4.dp)
                    .fillMaxWidth()
            )
            Divider(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            )
        }
    }
}