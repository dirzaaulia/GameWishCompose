package com.dirzaaulia.gamewish.ui.common

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.utils.visible
import com.dirzaaulia.gamewish.ui.home.HomeViewModel
import com.dirzaaulia.gamewish.utils.MyAnimeListConstant
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState
import kotlinx.coroutines.launch
import okhttp3.HttpUrl
import timber.log.Timber

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "SetJavaScriptEnabled")
@Composable
fun MyAnimeListWebViewClient(
    from: Int,
    viewModel: HomeViewModel,
    upPress: () -> Unit = {}
) {

    val errorState = remember { mutableStateOf("") }
    val progressShow = remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    var reload: () -> Unit = {}

    val initialUrl = HttpUrl.Builder()
        .scheme("https")
        .host(MyAnimeListConstant.MYANIMELIST_URL)
        .addPathSegment("v1")
        .addPathSegment("oauth2")
        .addPathSegment("authorize")
        .addQueryParameter("response_type", "code")
        .addQueryParameter("client_id", MyAnimeListConstant.MYANIMELIST_CLIENT_ID)
        .addQueryParameter("code_challenge", MyAnimeListConstant.MYANIMELIST_CODE_CHALLENGE)
        .addQueryParameter("state", MyAnimeListConstant.MYANIMELIST_STATE)
        .build()
    val webViewState = rememberWebViewState(url = initialUrl.toString())
    val webViewNavigator = rememberWebViewNavigator()
    val webClient = remember {
        object : AccompanistWebViewClient() {

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

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val urlValue = request?.url.toString()
                val code = request?.url?.getQueryParameter("code")
                val error = request?.url?.getQueryParameter("error")
                Timber.d(urlValue)

                urlValue.let { url ->
                    if (url.contains(MyAnimeListConstant.MYANIMELIST_BASE_URL_CALLBACK)) {
                        if (code != null) {
                            viewModel.getMyAnimeListToken(
                                MyAnimeListConstant.MYANIMELIST_CLIENT_ID,
                                code,
                                MyAnimeListConstant.MYANIMELIST_CODE_CHALLENGE,
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
        }
    }

    val modifier : Modifier = if (from == 1) {
        Modifier
            .statusBarsPadding()
            .fillMaxSize()
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
            WebView(
                state = webViewState,
                navigator = webViewNavigator,
                onCreated = { webView ->
                    webView.settings.javaScriptEnabled = true
                },
                client = webClient
            )
        } else {
            ErrorConnect(
                text = stringResource(id = R.string.no_connection)
            ) {
                reload()
                errorState.value = ""
            }
        }
    }
}