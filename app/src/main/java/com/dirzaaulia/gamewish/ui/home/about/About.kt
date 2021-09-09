package com.dirzaaulia.gamewish.ui.home.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.data.model.myanimelist.User
import com.dirzaaulia.gamewish.ui.home.HomeViewModel
import com.dirzaaulia.gamewish.utils.NetworkImage
import com.google.accompanist.insets.statusBarsPadding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun About(
    viewModel: HomeViewModel,
    googleProfileImage: String,
    googleUsername: String,
    myAnimeListUser: User,
) {
    Scaffold(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp),
        topBar = {
            AboutAppBar()
        }
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            GoogleSection(
                googleProfileImage = googleProfileImage,
                googleUsername = googleUsername
            )
            MyAnimeListSection(myAnimeListUser = myAnimeListUser)
        }
    }
}

@Composable
fun GoogleSection(
    googleProfileImage: String,
    googleUsername: String
) {
    Card(
        modifier = Modifier.padding(8.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = "Google Account")
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
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
                Text(
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f),
                    text = googleUsername,
                    style = MaterialTheme.typography.h6
                )
            }
        }
    }
}

@Composable
fun MyAnimeListSection(myAnimeListUser: User) {
    Card(
        modifier = Modifier.padding(8.dp),
        shape = MaterialTheme.shapes.large
    ) {
        myAnimeListUser.id?.let {

        }
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = "MyAnimeList Account")
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
                myAnimeListUser.name?.let {
                    Text(
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f),
                        text = it,
                        style = MaterialTheme.typography.h6
                    )
                }
            }

            val buttonText: String = if (myAnimeListUser.id.isNullOrBlank()) {
                "Link MyAnimeList Account"
            } else {
                "Unlink MyAnimeList Account"
            }

            OutlinedButton(
                modifier = Modifier.padding(top = 4.dp).fillMaxWidth(),
                onClick = {
                },
                content = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        content = {
                            Text(
                                style = MaterialTheme.typography.button,
                                color = MaterialTheme.colors.onSurface,
                                text = buttonText
                            )
                        }
                    )
                }
            )
        }
    }
}

@Composable
fun AboutAppBar() {
    TopAppBar(
        elevation = 0.dp,
        modifier = Modifier
            .height(80.dp)
            .statusBarsPadding()
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
