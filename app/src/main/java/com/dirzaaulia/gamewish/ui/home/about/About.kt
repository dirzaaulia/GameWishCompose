package com.dirzaaulia.gamewish.ui.home.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.base.ResponseResult
import com.dirzaaulia.gamewish.data.model.myanimelist.User
import com.dirzaaulia.gamewish.extension.isError
import com.dirzaaulia.gamewish.extension.isSucceeded
import com.dirzaaulia.gamewish.ui.home.HomeViewModel
import com.dirzaaulia.gamewish.utils.FirebaseConstant
import com.dirzaaulia.gamewish.utils.NetworkImage
import com.dirzaaulia.gamewish.utils.openLink
import com.dirzaaulia.gamewish.utils.sendEmail
import com.google.accompanist.insets.statusBarsPadding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

@Composable
fun About(
    viewModel: HomeViewModel,
    googleProfileImage: String,
    googleUsername: String,
    myAnimeListUserResult: ResponseResult<User>?,
    myAnimeListUser: User,
    navigateToMyAnimeListLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val accessToken by viewModel.token.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            AboutAppBar()
        }
    ) {
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
        shape = MaterialTheme.shapes.large
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = "Google Account")
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Surface(
                    modifier = Modifier.padding(8.dp),
                    shape = CircleShape
                ) {
                    NetworkImage(
                        modifier = Modifier.size(75.dp),
                        url = googleProfileImage,
                        contentDescription = null
                    )
                }
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = googleUsername,
                        style = MaterialTheme.typography.h6
                    )
                    OutlinedButton(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .fillMaxWidth(),
                        onClick = {
                            viewModel.logoutGoogle()
                            googleSignInClient.signOut()
                            viewModel.selectBottomNavMenu(R.string.wishlist)
                        },
                    ) {
                        Text(
                            style = MaterialTheme.typography.button,
                            color = MaterialTheme.colors.onSurface,
                            text = "Logout Account"
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
        modifier = Modifier.padding(8.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = "MyAnimeList Account")
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
                                    modifier = Modifier.size(75.dp),
                                    url = it,
                                    contentDescription = null
                                )
                            }
                        }
                        Column{
                            myAnimeListUser.name?.let {
                                Text(
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth(),
                                    text = it,
                                    style = MaterialTheme.typography.h6
                                )
                            }
                            OutlinedButton(
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .fillMaxWidth(),
                                onClick = {
                                    viewModel.setAccessToken("")
                                    viewModel.getAccessToken()
                                },
                            ) {
                                Text(
                                    style = MaterialTheme.typography.button,
                                    color = MaterialTheme.colors.onSurface,
                                    text = "Unlink Account"
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
                                style = MaterialTheme.typography.button,
                                color = MaterialTheme.colors.onSurface,
                                text = "Link Account"
                            )
                        }
                    } else {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = R.string.myanimelist_user_error),
                            style = MaterialTheme.typography.subtitle1
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
        modifier = Modifier.padding(8.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = "About GameWish")
            ClickableText(
                style = MaterialTheme.typography.caption,
                modifier = Modifier.fillMaxWidth(),
                text = AnnotatedString(
                    stringResource(id = R.string.about_app),
                    SpanStyle(
                        color = MaterialTheme.colors.onSurface,
                        textDecoration = TextDecoration.Underline
                    ),
                ),
                onClick = { openLink(context, "https://linktr.ee/DirzaAulia") }
            )
            ClickableText(
                style = MaterialTheme.typography.caption,
                modifier = Modifier.fillMaxWidth(),
                text = AnnotatedString(
                    stringResource(id = R.string.about_app2),
                    SpanStyle(
                        color = MaterialTheme.colors.onSurface,
                        textDecoration = TextDecoration.Underline
                    ),
                ),
                onClick = { openLink(context, "https://play.google.com/store/apps/dev?id=4806849608818858118") }
            )
        }
    }
}

@Composable
fun DataSourceSection() {
    val context = LocalContext.current
    Card(
        modifier = Modifier.padding(8.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = "Data Source")
            ClickableText(
                style = MaterialTheme.typography.caption,
                modifier = Modifier.fillMaxWidth(),
                text = AnnotatedString(
                    stringResource(id = R.string.games_data_source),
                    SpanStyle(
                        color = MaterialTheme.colors.onSurface,
                        textDecoration = TextDecoration.Underline
                    ),
                ),
                onClick = { openLink(context, "https://www.rawg.io") }
            )
            ClickableText(
                style = MaterialTheme.typography.caption,
                modifier = Modifier.fillMaxWidth(),
                text = AnnotatedString(stringResource(
                    id = R.string.deals_data_source),
                    SpanStyle(
                        color = MaterialTheme.colors.onSurface,
                        textDecoration = TextDecoration.Underline
                    ),
                ),
                onClick = { openLink(context, "https://www.cheapshark.com/") }
            )
            ClickableText(
                style = MaterialTheme.typography.caption,
                modifier = Modifier.fillMaxWidth(),
                text = AnnotatedString(
                    stringResource(id = R.string.anime_manga_data_source),
                    SpanStyle(
                        color = MaterialTheme.colors.onSurface,
                        textDecoration = TextDecoration.Underline
                    ),
                ),
                onClick = { openLink(context, "https://wwww.myanimelist.net/") }
            )
            ClickableText(
                style = MaterialTheme.typography.caption,
                modifier = Modifier.fillMaxWidth(),
                text = AnnotatedString(
                    stringResource(id = R.string.movie_tv_show_data_source),
                    SpanStyle(
                        color = MaterialTheme.colors.onSurface,
                        textDecoration = TextDecoration.Underline
                    ),
                ),
                onClick = { openLink(context, "https://wwww.themoviedb.org/") }
            )
        }
    }
}

@Composable
fun ContactMeSection() {
    val context = LocalContext.current
    Card(
        modifier = Modifier.padding(8.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = "Contact")
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.about_contact),
                style = MaterialTheme.typography.caption
            )
            OutlinedButton(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .fillMaxWidth(),
                onClick = {
                    context.sendEmail()
                },
            ) {
                Text(
                    style = MaterialTheme.typography.button,
                    color = MaterialTheme.colors.onSurface,
                    text = "Send E-Mail"
                )
            }
        }
    }
}

@Composable
fun AboutAppBar() {
    TopAppBar(
        elevation = 0.dp,
        modifier = Modifier
            .statusBarsPadding()
            .wrapContentHeight()
    ) {
        Image(
            modifier = Modifier
                .padding(0.dp, 16.dp, 0.dp, 16.dp)
                .size(100.dp, 0.dp)
                .align(Alignment.CenterVertically)
                .aspectRatio(1.0f),
            painter = painterResource(id = R.drawable.ic_gamewish_dark),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
    }
}
