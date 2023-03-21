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
        data = Uri.parse(OtherConstant.EMAIL)
    }

    ContextCompat.startActivity(this, intent, null)
}

/**
 * Double
 */
fun Double?.replaceIfNull(replacementValue: Double = OtherConstant.ZERO_DOUBLE): Double {
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
    format.maximumFractionDigits = OtherConstant.TWO

    return format.format(this.replaceIfNull())
}

/**
 * Int
 */
fun Int?.replaceIfNull(replacementValue: Int = OtherConstant.ZERO): Int {
    if (this == null)
        return replacementValue
    return this
}

fun Int?.toAnimeScoreFormat(): String {
    return when (this.replaceIfNull()) {
        OtherConstant.ONE -> "(1) - Appaling"
        OtherConstant.TWO -> "(2) - Horrible"
        OtherConstant.THREE -> "(3) - Very Bad"
        OtherConstant.FOUR -> "(4) - Bad"
        OtherConstant.FIVE -> "(5) - Average"
        OtherConstant.SIX -> "(6) - Fine"
        OtherConstant.SEVEN -> "(7) - Good"
        OtherConstant.EIGHT -> "(8) - Very Good"
        OtherConstant.NINE -> "(9) - Great"
        OtherConstant.TEN -> "(10) - Masterpiece"
        else -> OtherConstant.EMPTY_STRING
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
    var value = OtherConstant.EMPTY_STRING
    this?.forEachIndexed { index, developer ->
        value += developer.name.toString()
        if (index != this.size - OtherConstant.ONE) {
            value += OtherConstant.NEWLINE
        }
    }
    return value
}

fun List<Publisher>?.toPublisher(): String {
    var value = OtherConstant.EMPTY_STRING
    this?.forEach {
        value += it.name.toString()
        value += OtherConstant.NEWLINE

        if (value.isNotEmpty()) {
            value = value.substring(
                OtherConstant.ZERO, value.length - OtherConstant.ONE
            )
        }
    }
    return value
}

fun List<MyAnimelistGenre>?.toAnimeGenre(): String {
    var genre = OtherConstant.EMPTY_STRING
    this?.forEach {
        genre += String.format(
            OtherConstant.STRING_FORMAT_S_S,
            it.name,
            OtherConstant.BLANK_SPACE
        )
    }
    return genre
}

fun List<ProductionCompany>?.toProductionCompany(): String {
    var genre = OtherConstant.EMPTY_STRING
    this?.forEachIndexed { index, productionCompany ->
        genre += if (index == (this.size - OtherConstant.ONE)) {
            productionCompany.name.toString()
        } else {
            String.format(
                OtherConstant.STRING_FORMAT_S_S,
                productionCompany.name.toString(),
                OtherConstant.NEWLINE
            )
        }
    }
    return genre
}

fun List<TmdbGenre>?.toMovieGenre(): String {
    var genre = OtherConstant.EMPTY_STRING
    this?.forEach {
        genre += String.format(
            OtherConstant.STRING_FORMAT_S_S,
            it.name,
            OtherConstant.BLANK_SPACE
        )
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
    split(OtherConstant.BLANK_SPACE)
        .joinToString(OtherConstant.BLANK_SPACE) { text ->
            text.lowercase(Locale.getDefault())
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        }


@SuppressLint("DefaultLocale")
fun String.lowerCaseWords(): String =
    split(OtherConstant.BLANK_SPACE)
        .joinToString(OtherConstant.BLANK_SPACE) {
            it.lowercase(Locale.getDefault())
        }

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
    val segment = uri.path?.split(OtherConstant.FORWARD_SLASH)
    return String.format(
        OtherConstant.STRING_FORMAT_REDDIT,
        segment?.get(segment.size - OtherConstant.TWO)
    )
}

fun String?.fromHtml(): String {
    val value = this.replaceIfNull()
    return Html.fromHtml(value, Html.FROM_HTML_MODE_COMPACT).toString()
}

fun String.changeDateFormat(fromFormat: String): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) newDateFormatter(
        this,
        fromFormat
    ) else oldDateFormatter(this, fromFormat)
}

fun String?.myAnimeListStatusFormatted(ifBlank: String = OtherConstant.EMPTY_STRING): String {
    return this
        .replaceIfNull()
        .replace(OtherConstant.UNDERSCORE, OtherConstant.BLANK_SPACE)
        .capitalizeWords()
        .ifBlank { ifBlank }
}

fun String.tmdbStatusFormatted(): String {
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

fun String.doBasedOnTmdbType(
    doIfMovie: () -> Unit,
    doIfTv: () -> Unit
) {
    if (this.equals(TmdbConstant.TMDB_TYPE_MOVIE, true))
        doIfMovie.invoke() else doIfTv.invoke()
}


fun String.setStringBasedOnTmdbType(
    setIfMovie: String,
    setIfTv: String
): String {
    return if (this.equals(TmdbConstant.TMDB_TYPE_MOVIE, true))
        setIfMovie else setIfTv
}

fun String?.formatTmdbReleaseDate(): String {
    return this
        .replaceIfNull()
        .changeDateFormat(OtherConstant.DATE_FORMAT_STRIP_yyyy_MM_dd)
        .ifBlank { TmdbConstant.TMDB_NO_RELEASE_DATE }
}