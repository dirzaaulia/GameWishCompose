package com.dirzaaulia.gamewish.ui.main.login

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.ui.home.HomeViewModel
import com.dirzaaulia.gamewish.utils.FirebaseConstant
import com.dirzaaulia.gamewish.utils.FirebaseState
import com.google.accompanist.insets.statusBarsPadding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun Login(viewModel: HomeViewModel) {

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val state by viewModel.loadingState.collectAsState()
    val context = LocalContext.current
    val token = FirebaseConstant.GOOGLE_SIGN_IN_WEB_CLIENT_ID

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
                viewModel.signWithCredential(credential)
            } catch (e: ApiException) {
                Timber.w("Google sign in failed: ${e.message}")
                scope.launch {
                    e.message?.let { message ->
                        scaffoldState.snackbarHostState.showSnackbar(
                            message
                        )
                    }
                }
            }
        }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        scaffoldState = scaffoldState,
        topBar = {
            if (state.status == FirebaseState.Status.RUNNING) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        },
        backgroundColor = MaterialTheme.colors.primarySurface,
    ) {
        when (state.status) {
            FirebaseState.Status.SUCCESS -> {
                val auth = viewModel.getFirebaseAuth()
                auth.currentUser?.uid?.let { uid -> viewModel.setUserAuthId(uid) }
                viewModel.getUserAuthStatus()
                auth.currentUser?.uid?.let { uid -> viewModel.syncWishlist(uid) }
                viewModel.getGoogleUserData()
            }
            FirebaseState.Status.FAILED -> {
                scope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(
                        "Login went error! Please try again later."
                    )
                }
            }
            else -> {
            }
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                painter = painterResource(id = R.drawable.ic_gamewish_dark),
                contentDescription = null,
                contentScale = ContentScale.Fit,
            )
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(token)
                        .requestEmail()
                        .build()

                    val googleSignInClient = GoogleSignIn.getClient(context, gso)
                    launcher.launch(googleSignInClient.signInIntent)
                },
                content = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        content = {
                            Icon(
                                tint = Color.Unspecified,
                                painter = painterResource(id = R.drawable.googleg_standard_color_18),
                                contentDescription = null,
                            )
                            Text(
                                style = MaterialTheme.typography.button,
                                color = MaterialTheme.colors.onSurface,
                                text = "Google"
                            )
                            Icon(
                                tint = Color.Transparent,
                                imageVector = Icons.Default.MailOutline,
                                contentDescription = null,
                            )
                        }
                    )
                }
            )
        }
    }
}