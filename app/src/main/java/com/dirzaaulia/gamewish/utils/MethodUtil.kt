package com.dirzaaulia.gamewish.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.view.View
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat.startActivity
import com.dirzaaulia.gamewish.data.model.rawg.Platforms
import com.dirzaaulia.gamewish.data.model.rawg.Stores
import com.dirzaaulia.gamewish.ui.theme.Green700
import com.dirzaaulia.gamewish.ui.theme.Grey700
import com.dirzaaulia.gamewish.ui.theme.LightBlue700
import com.dirzaaulia.gamewish.ui.theme.Red700
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import java.util.*

fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            Timber.i("NetworkCapabilities.TRANSPORT_CELLULAR")
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            Timber.i("NetworkCapabilities.TRANSPORT_WIFI")
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            Timber.i("NetworkCapabilities.TRANSPORT_ETHERNET")
            return true
        }
    }
    return false
}

fun showSnackbarShort(view: View, message: String) {
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
}

fun showSnackbarShortWithAnchor(view: View, anchorView: View, message: String) {
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT).setAnchorView(anchorView).show()
}


fun showInfiniteSnackbar(view: View, message: String) {
    Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).show()
}

fun openLink(context: Context, link: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(link)
    startActivity(context, intent, null)
}

fun openRawgLink(context: Context) {
    val url = "https://www.rawg.io"
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)
    startActivity(context, intent, null)
}

fun openMyAnimeListLink(context: Context) {
    val url = "https://www.myanimelist.net"
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)
    startActivity(context, intent, null)
}

fun openDealsId(context: Context, dealsId: String?) {
    val url = String.format("https://www.cheapshark.com/redirect?dealID=%s", dealsId)
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)
    startActivity(context, intent, null)
}

//fun setImageWithGlide(imgView: ImageView, imgUrl: String?) {
//    imgUrl?.let {
//        val imgUri = it.toUri().buildUpon().scheme("https").build()
//
//        val circularProgressDrawable = CircularProgressDrawable(imgView.context)
//        circularProgressDrawable.strokeWidth = 5f
//        circularProgressDrawable.centerRadius = 30f
//        circularProgressDrawable.setColorSchemeColors(ContextCompat.getColor(imgView.context, R.color.color_on_surface_emphasis_high))
//        circularProgressDrawable.start()
//
//        Glide.with(imgView.context)
//            .load(imgUri)
//            .placeholder(circularProgressDrawable)
//            .error(R.drawable.ic_baseline_broken_image_24)
//            .into(imgView)
//    }
//}

fun showAlertDialogWithoutButton(context: Context, title: String, message: String) {
    MaterialAlertDialogBuilder(context)
        .setTitle(title)
        .setMessage(message)
        .show()
}

@SuppressLint("DefaultLocale")
fun String.capitalizeWords(): String =
    split(" ").joinToString(" ") {
        it.lowercase(Locale.getDefault())
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }


@SuppressLint("DefaultLocale")
fun String.lowerCaseWords(): String =
    split(" ").joinToString(" ") { it.lowercase(Locale.getDefault()) }

fun setGameDetailsListItemBackground(data: Any, code: Int): Color {
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