package com.dirzaaulia.gamewish.utils

import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.Spanned
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.data.model.rawg.Developer
import com.dirzaaulia.gamewish.data.model.rawg.EsrbRating
import com.dirzaaulia.gamewish.data.model.rawg.Publisher
import timber.log.Timber
import vas.com.currencyconverter.CurrencyConverter
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

fun htmlToTextFormatter(value: String?): Spanned? {
    return Html.fromHtml(value, Html.FROM_HTML_MODE_COMPACT)
}

fun numberFormatter(value: Double): String {
    val format = NumberFormat.getNumberInstance(Locale.US)
    return format.format(value)
}

fun currencyFormatter(value: Double?, currency: Locale): String {
    val format: NumberFormat = NumberFormat.getCurrencyInstance(currency)
    format.maximumFractionDigits = 2

    return format.format(value)
}

fun textDateFormatter(value: String): String {

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val inputFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date: LocalDate = LocalDate.parse(value, inputFormat)
        val outputFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

        date.format(outputFormat)
    } else {
        val dateParser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date: Date = dateParser.parse(value)
        val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        dateFormatter.format(date)
    }
}

fun textDateFormatter2(value: String): String {

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val inputFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date: LocalDate = LocalDate.parse(value, inputFormat)
        val outputFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")

        date.format(outputFormat)
    } else {
        val dateParser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date: Date = dateParser.parse(value)
        val dateFormatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

        dateFormatter.format(date)
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