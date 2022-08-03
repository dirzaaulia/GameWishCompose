package com.dirzaaulia.gamewish.ui.common

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.extension.visible
import com.dirzaaulia.gamewish.ui.home.HomeViewModel
import com.dirzaaulia.gamewish.utils.MyAnimeListConstant.MYANIMELIST_BASE_URL_CALLBACK
import com.dirzaaulia.gamewish.utils.MyAnimeListConstant.MYANIMELIST_CLIENT_ID
import com.dirzaaulia.gamewish.utils.MyAnimeListConstant.MYANIMELIST_CODE_CHALLENGE
import com.dirzaaulia.gamewish.utils.MyAnimeListConstant.MYANIMELIST_STATE
import com.dirzaaulia.gamewish.utils.MyAnimeListConstant.MYANIMELIST_URL
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.launch
import okhttp3.HttpUrl
import timber.log.Timber

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewMyAnimeList(
    from: Int,
    viewModel: HomeViewModel,
    upPress: () -> Unit
) {

    val errorState = remember { mutableStateOf("") }
    var reload: () -> Unit = {}
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val progressShow = remember { mutableStateOf(false) }
    val initialUrl = HttpUrl.Builder()
        .scheme("https")
        .host(MYANIMELIST_URL)
        .addPathSegment("v1")
        .addPathSegment("oauth2")
        .addPathSegment("authorize")
        .addQueryParameter("response_type", "code")
        .addQueryParameter("client_id", MYANIMELIST_CLIENT_ID)
        .addQueryParameter("code_challenge", MYANIMELIST_CODE_CHALLENGE)
        .addQueryParameter("state", MYANIMELIST_STATE)
        .build()

    val modifier : Modifier = if (from == 1) {
        Modifier.statusBarsPadding().fillMaxSize()
    } else {
        Modifier.fillMaxSize()
    }


    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .visible(progressShow.value)
            )
        },
        modifier = modifier
    ) {
        if (errorState.value.isBlank()) {
            AndroidView(
                update = {
                    it.loadUrl(initialUrl.toString())
                },
                factory = {
                    WebView(it).apply {
                        settings.javaScriptEnabled = true

                        webViewClient = object : WebViewClient() {
                            override fun shouldOverrideUrlLoading(
                                view: WebView?,
                                request: WebResourceRequest?
                            ): Boolean {
                                val urlValue = request?.url.toString()
                                val code = request?.url?.getQueryParameter("code")
                                val error = request?.url?.getQueryParameter("error")
                                Timber.d(urlValue)

                                urlValue.let { url ->
                                    if (url.contains(MYANIMELIST_BASE_URL_CALLBACK)) {
                                        if (code != null) {
                                            viewModel.getMyAnimeListToken(
                                                MYANIMELIST_CLIENT_ID,
                                                code,
                                                MYANIMELIST_CODE_CHALLENGE,
                                                "authorization_code"
                                            )
                                            upPress()
                                        } else if (error != null) {
                                            scope.launch {
                                                scaffoldState.snackbarHostState.showSnackbar(error)
                                            }
                                            view?.loadUrl(initialUrl.toString())
                                        }
                                        return true
                                    }
                                }
                                return false
                            }

                            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                super.onPageStarted(view, url, favicon)
                                progressShow.value = true
                                Timber.i(url)
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                progressShow.value = false
                                Timber.i(url)
                            }

                            override fun onReceivedError(
                                view: WebView?,
                                request: WebResourceRequest?,
                                error: WebResourceError?
                            ) {
                                val errorDesc = error?.description.toString()
                                errorState.value = errorDesc
                                Timber.d("url: ${request?.url} | errorDesc : $errorDesc")
                                reload = { view?.reload() }
                            }
                        }
                    }
                })
        } else {
            ErrorConnect(text = stringResource(id = R.string.no_connection)) {
                reload()
                errorState.value = ""
            }
        }

    }
}