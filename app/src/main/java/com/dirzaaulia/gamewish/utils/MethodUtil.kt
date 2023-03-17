package com.dirzaaulia.gamewish.utils

import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat.startActivity
import com.dirzaaulia.gamewish.data.model.rawg.Platforms
import com.dirzaaulia.gamewish.data.model.rawg.Stores
import com.dirzaaulia.gamewish.theme.Green700
import com.dirzaaulia.gamewish.theme.Grey700
import com.dirzaaulia.gamewish.theme.LightBlue700
import com.dirzaaulia.gamewish.theme.Red700
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun openLink(context: Context, link: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(link)
    startActivity(context, intent, null)
}

fun openDeals(context: Context, dealsId: String?) {
    val url = String.format(CheapSharkConstant.CHEAPSHARK_URL, dealsId)
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)
    startActivity(context, intent, null)
}

@RequiresApi(Build.VERSION_CODES.O)
fun newDateFormatter(
    value: String,
    fromFormat: String
): String {
    val date: LocalDate
    return try {
        val inputFormat: DateTimeFormatter = DateTimeFormatter.ofPattern(fromFormat, Locale.US)
        date = LocalDate.parse(value, inputFormat)
        val outputFormat: DateTimeFormatter = DateTimeFormatter.ofPattern(
            OtherConstant.DATE_FORMAT_STRIP_dd_MM_yyyy,
            Locale.US
        )
        date.format(outputFormat)
    } catch (exception: DateTimeParseException) {
        exception.printStackTrace()
        OtherConstant.EMPTY_STRING
    }
}

fun oldDateFormatter(
    value: String,
    fromFormat: String
): String {
    val date: Date
    return try {
        val dateParser = SimpleDateFormat(fromFormat, Locale.US)
        date = dateParser.parse(value)
        val dateFormatter = SimpleDateFormat(
            OtherConstant.DATE_FORMAT_STRIP_dd_MM_yyyy,
            Locale.US
        )
        dateFormatter.format(date)
    } catch (exception: Exception) {
        exception.printStackTrace()
        OtherConstant.EMPTY_STRING
    }
}

fun animeDateFormat(startDate: String, endDate: String): String {
    startDate
        .changeDateFormat(OtherConstant.DATE_FORMAT_STRIP_yyyy_MM_dd)
        .ifEmpty { startDate.changeDateFormat(OtherConstant.DATE_FORMAT_STRIP_yyyy_MM) }

    endDate
        .changeDateFormat(OtherConstant.DATE_FORMAT_STRIP_yyyy_MM_dd)
        .ifEmpty { startDate.changeDateFormat(OtherConstant.DATE_FORMAT_STRIP_yyyy_MM) }

    return if (endDate.isBlank()) {
        String.format(OtherConstant.STRING_FORMAT_S_STRIP_S, startDate, OtherConstant.NOW)
    } else {
        String.format(OtherConstant.STRING_FORMAT_S_STRIP_S, startDate, endDate)
    }
}

fun setPlatformsBackgroundColor(data: Any, code: Int): Color {
    if (code == OtherConstant.ZERO) {
        val platforms = data as Platforms
        val name = platforms.platform?.name

        return when {
            name?.contains(PlatformsConstant.XBOX) == true
                    || name?.contains(PlatformsConstant.ANDROID) == true -> {
                Green700
            }

            name?.contains(PlatformsConstant.PLAYSTATION) == true
                    || name?.contains(PlatformsConstant.PS) == true -> {
                LightBlue700
            }

            name?.contains(PlatformsConstant.NINTENDO) == true
                    || name?.contains(PlatformsConstant.WII) == true
                    || name?.contains(PlatformsConstant.NES) == true -> {
                Red700
            }

            else -> Grey700
        }
    } else {
        val stores = data as Stores
        val name = stores.store?.name

        return when {
            name?.contains(PlatformsConstant.XBOX) == true
                    || name?.contains(PlatformsConstant.ANDROID) == true -> Green700

            name?.contains(PlatformsConstant.PLAYSTATION) == true
                    || name?.contains(PlatformsConstant.PS) == true -> LightBlue700

            name?.contains(PlatformsConstant.NINTENDO) == true
                    || name?.contains(PlatformsConstant.WII) == true
                    || name?.contains(PlatformsConstant.NES) == true -> Red700

            else -> Grey700
        }
    }
}

fun getAnimeSeason(): String {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val season =   when (calendar.get(Calendar.MONTH)) {
        in OtherConstant.ZERO..OtherConstant.TWO -> MyAnimeListConstant.MYANIMELIST_SEASON_WINTER
        in OtherConstant.THREE..OtherConstant.FIVE -> MyAnimeListConstant.MYANIMELIST_SEASON_SPRING
        in OtherConstant.SIX..OtherConstant.EIGHT -> MyAnimeListConstant.MYANIMELIST_SEASON_SUMMER
        in OtherConstant.NINE..OtherConstant.ELEVEN -> MyAnimeListConstant.MYANIMELIST_SEASON_FALL
        else -> OtherConstant.EMPTY_STRING
    }
    return String.format(OtherConstant.STRING_FORMAT_S_SPACE_S, year, season)
}