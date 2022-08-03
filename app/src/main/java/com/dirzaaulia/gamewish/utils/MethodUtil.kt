package com.dirzaaulia.gamewish.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat.startActivity
import com.dirzaaulia.gamewish.data.model.rawg.Platforms
import com.dirzaaulia.gamewish.data.model.rawg.Stores
import com.dirzaaulia.gamewish.theme.Green700
import com.dirzaaulia.gamewish.theme.Grey700
import com.dirzaaulia.gamewish.theme.LightBlue700
import com.dirzaaulia.gamewish.theme.Red700

fun openLink(context: Context, link: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(link)
    startActivity(context, intent, null)
}

fun openDeals(context: Context, dealsId: String?) {
    val url = String.format("https://www.cheapshark.com/redirect?dealID=%s", dealsId)
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)
    startActivity(context, intent, null)
}

fun animeDateFormat(startDate: String?, endDate: String?): String {
    var startDateFormatted = ""
    var endDateFormatted = ""

    if (startDate != null) {
        startDateFormatted = startDate
            .changeDateFormat("yyyy-MM-dd")
            .ifEmpty { startDate.changeDateFormat("yyyy-MM") }
    }

    if (endDate != null) {
        endDateFormatted = endDate
            .changeDateFormat("yyyy-MM-dd")
            .ifEmpty { endDate.changeDateFormat("yyyy-MM") }
    }


    return if (endDateFormatted.isBlank()) {
        "$startDateFormatted - now"
    } else {
        "$startDateFormatted - $endDateFormatted"
    }
}

fun setPlatformsBackgroundColor(data: Any, code: Int): Color {
    if (code == 0) {
        val platforms = data as Platforms
        val name = platforms.platform?.name

        return when {
            name?.contains("Xbox") == true || name?.contains("Android") == true -> {
                Green700
            }
            name?.contains("PlayStation") == true || name?.contains("PS") == true -> {
                LightBlue700
            }
            name?.contains("Nintendo") == true || name?.contains("Wii") == true
                    || name?.contains("NES") == true -> {
                Red700
            }
            else -> Grey700
        }
    } else {
        val stores = data as Stores
        val name = stores.store?.name

        return when {
            name?.contains("Xbox") == true || name?.contains("Android") == true -> {
                Green700
            }
            name?.contains("PlayStation") == true || name?.contains("PS") == true -> {
                LightBlue700
            }
            name?.contains("Nintendo") == true || name?.contains("Wii") == true
                    || name?.contains("NES") == true -> {
                Red700
            }
            else -> Grey700
        }
    }
}