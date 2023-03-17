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
import com.dirzaaulia.gamewish.ui.home.HomeViewModel
import com.dirzaaulia.gamewish.utils.MyAnimeListConstant
import com.dirzaaulia.gamewish.utils.OtherConstant
import com.dirzaaulia.gamewish.utils.visible
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState
import kotlinx.coroutines.launch
import okhttp3.HttpUrl

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "SetJavaScriptEnabled")
@Composable
fun MyAnimeListWebViewClient(
    from: Int,
    viewModel: HomeViewModel,
    upPress: () -> Unit = {}
) {

    val errorState = remember { mutableStateOf(OtherConstant.EMPTY_STRING) }
    val progressShow = remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    var reload: () -> Unit = {}

    val initialUrl = HttpUrl.Builder()
        .scheme(MyAnimeListConstant.MYANIMELIST_SCHEME)
        .host(MyAnimeListConstant.MYANIMELIST_URL)
        .addPathSegment(MyAnimeListConstant.MYANIMELIST_PATH_SEGMENT_1)
        .addPathSegment(MyAnimeListConstant.MYANIMELIST_PATH_SEGMENT_2)
        .addPathSegment(MyAnimeListConstant.MYANIMELIST_PATH_SEGMENT_3)
        .addQueryParameter(
            name = MyAnimeListConstant.MYANIMELIST_QUERY_NAME_1,
            value = MyAnimeListConstant.MYANIMELIST_QUERY_VALUE_1
        )
        .addQueryParameter(
            name = MyAnimeListConstant.MYANIMELIST_QUERY_NAME_2,
            value = MyAnimeListConstant.MYANIMELIST_CLIENT_ID
        )
        .addQueryParameter(
            name = MyAnimeListConstant.MYANIMELIST_QUERY_NAME_3,
            value = MyAnimeListConstant.MYANIMELIST_CODE_CHALLENGE
        )
        .addQueryParameter(
            name = MyAnimeListConstant.MYANIMELIST_QUERY_NAME_4,
            value = MyAnimeListConstant.MYANIMELIST_STATE
        )
        .build()
    val webViewState = rememberWebViewState(url = initialUrl.toString())
    val webViewNavigator = rememberWebViewNavigator()
    val webClient = remember {
        object : AccompanistWebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressShow.value = true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressShow.value = false
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                val errorDesc = error?.description.toString()
                errorState.value = errorDesc
                reload = { view?.reload() }
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val urlValue = request?.url.toString()
                val code = request?.url?.getQueryParameter(
                    MyAnimeListConstant.MYANIMELIST_QUERY_ERROR_1
                )
                val error = request?.url?.getQueryParameter(
                    MyAnimeListConstant.MYANIMELIST_QUERY_ERROR_2
                )

                urlValue.let { url ->
                    if (url.contains(MyAnimeListConstant.MYANIMELIST_BASE_URL_CALLBACK)) {
                        if (code != null) {
                            viewModel.getMyAnimeListToken(
                                MyAnimeListConstant.MYANIMELIST_CLIENT_ID,
                                code,
                                MyAnimeListConstant.MYANIMELIST_CODE_CHALLENGE,
                                MyAnimeListConstant.MYANIMELIST_GRANT_TYPE
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

    val modifier : Modifier = if (from == MyAnimeListConstant.MYANIMELIST_WEBVIEW_OTHER) {
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
                errorState.value = OtherConstant.EMPTY_STRING
            }
        }
    }
}