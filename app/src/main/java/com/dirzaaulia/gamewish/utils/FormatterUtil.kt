package com.dirzaaulia.gamewish.utils

import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.Spanned
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.data.model.myanimelist.Genre
import com.dirzaaulia.gamewish.data.model.rawg.Developer
import com.dirzaaulia.gamewish.data.model.rawg.EsrbRating
import com.dirzaaulia.gamewish.data.model.rawg.Publisher
import timber.log.Timber
import vas.com.currencyconverter.CurrencyConverter
import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

fun htmlToTextFormatter(value: String?): Spanned? {
    value.let {
        return Html.fromHtml(value, Html.FROM_HTML_MODE_COMPACT)
    }
}

fun numberFormatter(value: Double?): String {
    value.let {
        val format = NumberFormat.getNumberInstance(Locale.US)
        return format.format(value)
    }
}

fun currencyFormatter(value: Double?, currency: Locale): String {
    val format: NumberFormat = NumberFormat.getCurrencyInstance(currency)
    format.maximumFractionDigits = 2

    return format.format(value)
}

fun textDateFormatter(value: String): String {

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val inputFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US)
        val date: LocalDate = LocalDate.parse(value, inputFormat)
        val outputFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.US)

        date.format(outputFormat)
    } else {
        val dateParser = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val date: Date = dateParser.parse(value)
        val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.US)

        dateFormatter.format(date)
    }
}

fun textDateFormatter2(value: String?): String {
    value.let {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val inputFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US)
            val date: LocalDate = LocalDate.parse(value, inputFormat)
            val outputFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.US)

            date.format(outputFormat)
        } else {
            val dateParser = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val date: Date = dateParser.parse(value)
            val dateFormatter = SimpleDateFormat("dd MMMM yyyy", Locale.US)

            dateFormatter.format(date)
        }
    }
}

fun gameDeveloperFormatter(developers: List<Developer>): String {
    var value = ""
    developers.forEach {
        value += it.name.toString()
        value += "\n"

        if (value.isNotEmpty()) {
            value = value.substring(0, value.length - 1)
        }
    }
    return value
}

fun gamePublishersFormatter(publishers: List<Publisher>): String {
    var value = ""
    publishers.forEach {
        value += it.name.toString()
        value += "\n"

        if (value.isNotEmpty()) {
            value = value.substring(0, value.length - 1)
        }
    }
    return value
}

fun esrbRatingFormatter(esrbRating: EsrbRating): Int {
    return when (esrbRating.name) {
        "Everyone" -> {
            R.drawable.image_esrb_rating_everyone
        }
        "Everyone 10+" -> {
            R.drawable.image_esrb_rating_everyone10
        }
        "Teen" -> {
            R.drawable.image_esrb_rating_teen
        }
        "Mature" -> {
            R.drawable.image_esrb_rating_mature
        }
        "Adults Only" -> {
            R.drawable.image_esrb_rating_adults_only
        }
        "Rating Pending" -> {
            R.drawable.image_esrb_rating_pending
        }
        else -> R.drawable.image_esrb_rating_everyone
    }
}

fun getSubReddit(url: String): String {
    val uri = Uri.parse(url)
    val segment = uri.path?.split("/")
    return String.format("r/%s", segment?.get(segment.size - 2))
}

fun currencyConvertAndFormat(
    from: String,
    to: String,
    price: String
): String {
    var formattedPrice = ""
    val localeCurrent = Locale.getDefault()
    localeCurrent.let {
        CurrencyConverter.calculate(
            price.toDouble(),
            Currency.getInstance(from),
            Currency.getInstance(to),
        ) { value, e ->
            if (e != null) {
                Timber.i(e.localizedMessage)
            } else {
                formattedPrice = currencyFormatter(value, Locale.getDefault())
            }
        }
    }
    return formattedPrice
}

fun animeSourceFormat(value: String?) : String {
    value.let {
        return when (it) {
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
}

fun animeDateFormat(startDate: String?, endDate: String?): String {
    var startDateFormatted = ""
    var endDateFormatted = ""
    if (startDate != null) {
        startDateFormatted = textDateFormatter2(startDate)
    }

    if (endDate != null) {
        endDateFormatted = textDateFormatter2(endDate)
    }


    return if (endDateFormatted.isBlank()) {
        "$startDateFormatted - now"
    } else {
        "$startDateFormatted - $endDateFormatted"
    }
}

fun animeRatingFormat(value: String?) : String {
    value.let {
        return when (it) {
            "g" -> "All Ages"
            "pg" -> "Children"
            "pg_13" -> "Teens 13 or older"
            "r" -> "17+ (violence & profanity)"
            "r+" -> "Mild Nudity"
            "rx" -> "Hentai"
            else -> ""
        }
    }
}

fun animeGenreFormat(list: List<Genre>?): String {
    if (list?.isNotEmpty() == true) {
        var genre: String = ""
        list.forEach {
            genre += "${it.name} "
        }
        return genre
    }
    return ""
}