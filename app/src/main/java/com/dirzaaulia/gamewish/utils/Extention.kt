package com.dirzaaulia.gamewish.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.text.Html
import androidx.core.content.ContextCompat
import com.dirzaaulia.gamewish.data.model.myanimelist.Genre as MyAnimelistGenre
import com.dirzaaulia.gamewish.data.model.rawg.Developer
import com.dirzaaulia.gamewish.data.model.rawg.Publisher
import com.dirzaaulia.gamewish.data.model.tmdb.ProductionCompany
import com.dirzaaulia.gamewish.data.model.tmdb.ServiceCode
import com.dirzaaulia.gamewish.data.model.tmdb.Genre as TmdbGenre
import java.text.NumberFormat
import java.util.*

/**
 * Context
 */
fun Context.sendEmail() {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:dirzaaulia11@gmail.com" )
    }

    ContextCompat.startActivity(this, intent, null)
}

/**
 * Double
 */
fun Double?.replaceIfNull(replacementValue: Double = 0.0): Double {
    if (this == null)
        return replacementValue
    return this
}

fun Double?.toNumberFormat(): String {
    val format = NumberFormat.getNumberInstance(Locale.US)
    return format.format(this.replaceIfNull())
}

fun Double?.toCurrencyFormat(locale: Locale = Locale.US): String {
    val format: NumberFormat = NumberFormat.getCurrencyInstance(locale)
    format.maximumFractionDigits = 2

    return format.format(this.replaceIfNull())
}

/**
 * Int
 */
fun Int?.replaceIfNull(replacementValue: Int = 0): Int {
    if (this == null)
        return replacementValue
    return this
}

fun Int?.toAnimeScoreFormat(): String {
    return when (this.replaceIfNull()) {
        1 -> "(1) - Appaling"
        2 -> "(2) - Horrible"
        3 -> "(3) - Very Bad"
        4 -> "(4) - Bad"
        5 -> "(5) - Average"
        6 -> "(6) - Fine"
        7 -> "(7) - Good"
        8 -> "(8) - Very Good"
        9 -> "(9) - Great"
        10 -> "(10) - Masterpiece"
        else -> ""
    }
}

/**
 * List
 */
fun <T> List<T>?.replaceIfNull(replacementValue: List<T> = emptyList()): List<T> {
    if (this == null) {
        return replacementValue
    }
    return this
}

fun List<Developer>?.toDeveloper(): String {
    var value = ""
    this?.forEachIndexed { index, developer ->
        value += developer.name.toString()
        if (index != this.size - 1) {
            value += "\n"
        }
    }
    return value
}

fun List<Publisher>?.toPublisher(): String {
    var value = ""
    this?.forEach {
        value += it.name.toString()
        value += "\n"

        if (value.isNotEmpty()) {
            value = value.substring(0, value.length - 1)
        }
    }
    return value
}

fun List<MyAnimelistGenre>?.toAnimeGenre(): String {
    var genre = ""
    this?.forEach {
        genre += "${it.name} "
    }
    return genre
}

fun List<ProductionCompany>?.toProductionCompany(): String {
    var genre = ""
    this?.forEachIndexed { index, productionCompany ->
        genre += if (index == (this.size - 1)) {
            "${productionCompany.name}"
        } else {
            "${productionCompany.name}\n"
        }
    }
    return genre
}

fun List<TmdbGenre>?.toMovieGenre(): String {
    var genre = ""
    this?.forEach {
        genre += "${it.name} "
    }
    return genre
}

/**
 * String
 */
fun String?.replaceIfNull(replacementValue: String = OtherConstant.EMPTY_STRING): String {
    if (this == null)
        return replacementValue
    return this
}

@SuppressLint("DefaultLocale")
fun String.capitalizeWords(): String =
    split(" ").joinToString(" ") { text ->
        text.lowercase(Locale.getDefault())
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }


@SuppressLint("DefaultLocale")
fun String.lowerCaseWords(): String =
    split(" ").joinToString(" ") { it.lowercase(Locale.getDefault()) }

fun String?.animeRatingFormat(): String {
    return when (this.replaceIfNull()) {
        "g" -> "All Ages"
        "pg" -> "Children"
        "pg_13" -> "Teens 13 or older"
        "r" -> "17+ (violence & profanity)"
        "r+" -> "Mild Nudity"
        "rx" -> "Hentai"
        else -> ""
    }
}

fun String?.animeSourceFormat(): String {
    return when (this.replaceIfNull()) {
        "tv" -> "TV"
        "ova" -> "OVA"
        "movie" -> "Movie"
        "special" -> "Special"
        "ona" -> "ONA"
        "music" -> "Music"
        "manga" -> "Manga"
        "one_shot" -> "One-Shot"
        "doujinshi" -> "Doujinshi"
        "light_novel" -> "Light Novel"
        "novel" -> "Novel"
        "manhwa" -> "Manhwa"
        "manhua" -> "Manhua"
        else -> ""
    }
}

fun String?.getSubReddit(): String {
    val uri = Uri.parse(this)
    val segment = uri.path?.split("/")
    return String.format("r/%s", segment?.get(segment.size - 2))
}

fun String?.fromHtml(): String {
    val value = this.replaceIfNull()
    return Html.fromHtml(value, Html.FROM_HTML_MODE_COMPACT).toString()
}

fun String.changeDateFormat(fromFormat: String): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) newDateFormatter(this, fromFormat) else oldDateFormatter(this, fromFormat)
}

fun String?.myAnimeListStatusFormatted(ifBlank: String): String {
    return this
        .replaceIfNull()
        .replace(OtherConstant.UNDERSCORE, OtherConstant.BLANK_SPACE)
        .capitalizeWords()
        .ifBlank { ifBlank }
}

fun String.movieStatusFormatted(): String {
    return this.ifBlank { TmdbConstant.TMBD_STATUS_ALL }
}

fun String.getTmdbRecomendations(): ServiceCode {
    return if (this.equals(TmdbConstant.TMDB_TYPE_MOVIE, true))
        ServiceCode.MOVIE_RECOMMENDATIONS else ServiceCode.TV_RECOMMENDATIONS
}

fun String?.checkContain(contain: String): Boolean {
    return this.replaceIfNull()
        .contains(contain, true)
}

fun String?.toMyAnimeListSource(): String {
    return String.format(
        OtherConstant.STRING_FORMAT_S_SPACE_S_SPACE_S,
        MyAnimeListConstant.MYANIMELIST_SOURCE,
        OtherConstant.COLON,
        this.replaceIfNull()
            .replace(OtherConstant.UNDERSCORE, OtherConstant.BLANK_SPACE)
            .capitalizeWords()
    )
}

fun String.myAnimeListStatusApiFormat(): String {
    return this
        .lowerCaseWords()
        .replace(OtherConstant.STRIP, OtherConstant.BLANK_SPACE)
        .replace(OtherConstant.BLANK_SPACE, OtherConstant.UNDERSCORE)
}

fun String.setStringBasedOnMyAnimeListType(
    setIfAnime: String,
    setIfManga: String
): String {
    return if (this.equals(MyAnimeListConstant.MYANIMELIST_TYPE_ANIME, true))
        setIfAnime else setIfManga
}

fun String.setListBasedOnMyAnimeListType(
    setIfAnime: List<String>,
    setIfManga: List<String>
): List<String> {
    return if (this.equals(MyAnimeListConstant.MYANIMELIST_TYPE_ANIME, true))
        setIfAnime else setIfManga
}

fun String.doBasedOnMyAnimeListType(
    doIfAnime: () -> Unit,
    doIfManga: () -> Unit
) {
    if (this.equals(MyAnimeListConstant.MYANIMELIST_TYPE_ANIME, true))
        doIfAnime.invoke() else doIfManga.invoke()
}

fun String.setStringBasedOnTmdbStatus(
    setIfMovie: String,
    setIfTv: String
): String {
    return if (this.equals(TmdbConstant.TMDB_TYPE_MOVIE, true))
        setIfMovie else setIfTv
}