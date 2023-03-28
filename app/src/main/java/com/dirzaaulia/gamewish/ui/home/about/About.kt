package com.dirzaaulia.gamewish.ui.home.about

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.data.model.myanimelist.User
import com.dirzaaulia.gamewish.ui.home.HomeViewModel
import com.dirzaaulia.gamewish.utils.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun About(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
    googleProfileImage: String,
    googleUsername: String,
    myAnimeListUserResult: ResponseResult<User>?,
    myAnimeListUser: User,
    navigateToMyAnimeListLogin: () -> Unit,
) {

    val accessToken by viewModel.token.collectAsState()

    Scaffold(modifier = modifier) {
        LazyColumn {
            item {
                GoogleSection(
                    googleProfileImage = googleProfileImage,
                    googleUsername = googleUsername,
                    viewModel = viewModel
                )
            }
            item {
                MyAnimeListSection(
                    accessToken = accessToken,
                    myAnimeListUserResult = myAnimeListUserResult,
                    myAnimeListUser = myAnimeListUser,
                    viewModel = viewModel,
                    navigateToMyAnimeListLogin = navigateToMyAnimeListLogin
                )
            }
            item {
                GameWishAboutSection()
            }
            item {
                DataSourceSection()
            }
            item {
                ContactMeSection()
            }
        }
    }
}

@Composable
fun GoogleSection(
    googleProfileImage: String,
    googleUsername: String,
    viewModel: HomeViewModel
) {

    val context = LocalContext.current
    val token = FirebaseConstant.GOOGLE_SIGN_IN_WEB_CLIENT_ID
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(token)
        .requestEmail()
        .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    Card(
        modifier = Modifier.padding(8.dp),
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = OtherConstant.GOOGLE_ACCOUNT)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Surface(
                    modifier = Modifier.padding(8.dp),
                    shape = CircleShape
                ) {
                    NetworkImage(
                        url = googleProfileImage,
                        contentDescription = OtherConstant.EMPTY_STRING,
                        modifier = Modifier.size(75.dp)
                    )
                }
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = googleUsername,
                        style = MaterialTheme.typography.titleMedium
                    )
                    OutlinedButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                        onClick = {
                            viewModel.logoutGoogle()
                            googleSignInClient.signOut()
                            viewModel.selectBottomNavMenu(R.string.wishlist)
                        },
                        contentPadding = PaddingValues(4.dp)
                    ) {
                        Text(
                            text = OtherConstant.LOGOUT_ACCOUNT
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MyAnimeListSection(
    accessToken: String,
    myAnimeListUserResult: ResponseResult<User>?,
    myAnimeListUser: User,
    viewModel: HomeViewModel,
    navigateToMyAnimeListLogin: () -> Unit,
) {

    Card(
        modifier = Modifier.padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = MyAnimeListConstant.MYANIMELIST_ACCOUNT)
            when {
                myAnimeListUserResult.isSucceeded -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        myAnimeListUser.picture?.let {
                            Surface(
                                modifier = Modifier.padding(8.dp),
                                shape = CircleShape
                            ) {
                                NetworkImage(
                                    url = it,
                                    contentDescription = OtherConstant.EMPTY_STRING,
                                    modifier = Modifier.size(75.dp)
                                )
                            }
                        }
                        Column{
                            myAnimeListUser.name?.let { username ->
                                Text(
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth(),
                                    text = username,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                            OutlinedButton(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                                onClick = {
                                    viewModel.setAccessToken(OtherConstant.EMPTY_STRING)
                                    viewModel.getAccessToken()
                                },
                                contentPadding = PaddingValues(4.dp),
                            ) {
                                Text(
                                    text = OtherConstant.UNLINK_ACOCUNT
                                )
                            }
                        }
                    }
                }
                myAnimeListUserResult.isError -> {
                    if (accessToken.isBlank()) {
                        OutlinedButton(
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .fillMaxWidth(),
                            onClick = { navigateToMyAnimeListLogin() },
                        ) {
                            Text(
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                text = OtherConstant.LINK_ACCOUNT
                            )
                        }
                    } else {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = R.string.myanimelist_user_error),
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GameWishAboutSection() {
    val context = LocalContext.current
    Card(
        modifier = Modifier.padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = OtherConstant.ABOUT_GAMEWISH)
            ClickableText(
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.fillMaxWidth(),
                text = AnnotatedString(
                    stringResource(id = R.string.about_app),
                    SpanStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        textDecoration = TextDecoration.Underline
                    ),
                ),
                onClick = { openLink(context, OtherConstant.WEBSITE) }
            )
            ClickableText(
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.fillMaxWidth(),
                text = AnnotatedString(
                    stringResource(id = R.string.about_app2),
                    SpanStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        textDecoration = TextDecoration.Underline
                    ),
                ),
                onClick = { openLink(context, OtherConstant.PLAY_STORE_LINK) }
            )
        }
    }
}

@Composable
fun DataSourceSection() {
    val context = LocalContext.current
    Card(
        modifier = Modifier.padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = OtherConstant.DATA_SOURCE)
            ClickableText(
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.fillMaxWidth(),
                text = AnnotatedString(
                    stringResource(id = R.string.games_data_source),
                    SpanStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        textDecoration = TextDecoration.Underline
                    ),
                ),
                onClick = { openLink(context, OtherConstant.RAWG_LINK) }
            )
            ClickableText(
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.fillMaxWidth(),
                text = AnnotatedString(stringResource(
                    id = R.string.deals_data_source),
                    SpanStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        textDecoration = TextDecoration.Underline
                    ),
                ),
                onClick = { openLink(context, OtherConstant.CHEAPSHARK_LINK) }
            )
            ClickableText(
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.fillMaxWidth(),
                text = AnnotatedString(
                    stringResource(id = R.string.anime_manga_data_source),
                    SpanStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        textDecoration = TextDecoration.Underline
                    ),
                ),
                onClick = { openLink(context, OtherConstant.MYANIMELIST_LINK) }
            )
            ClickableText(
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.fillMaxWidth(),
                text = AnnotatedString(
                    stringResource(id = R.string.movie_tv_show_data_source),
                    SpanStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        textDecoration = TextDecoration.Underline
                    ),
                ),
                onClick = { openLink(context, OtherConstant.TMDB_LINK) }
            )
        }
    }
}

@Composable
fun ContactMeSection() {
    val context = LocalContext.current
    Card(
        modifier = Modifier.padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = stringResource(R.string.contact))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.about_contact),
                style = MaterialTheme.typography.labelSmall
            )
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                onClick = {
                    context.sendEmail()
                },
            ) {
                Text(
                    text = stringResource(R.string.send_e_mail)
                )
            }
        }
    }
}