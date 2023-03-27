package com.dirzaaulia.gamewish.ui.login

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.ui.home.HomeViewModel
import com.dirzaaulia.gamewish.utils.FirebaseConstant
import com.dirzaaulia.gamewish.utils.FirebaseState
import com.dirzaaulia.gamewish.utils.OtherConstant
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Login(viewModel: HomeViewModel) {

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
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
                scope.launch {
                    snackbarHostState.showSnackbar(e.message.toString())
                }
            }
        }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (state.status == FirebaseState.Status.RUNNING) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        when (state.status) {
            FirebaseState.Status.SUCCESS -> {
                val auth = viewModel.getFirebaseAuth()
                auth.currentUser?.uid?.let { uid -> viewModel.setUserAuthId(uid) }
                viewModel.getUserAuthStatus()
                auth.currentUser?.uid?.let { uid ->
                    viewModel.syncGameWishlist(uid)
                    viewModel.syncMovieWishlist(uid)
                }
                viewModel.getGoogleUserData()
            }
            FirebaseState.Status.FAILED -> {
                LaunchedEffect(FirebaseState) {
                    snackbarHostState.showSnackbar(
                        FirebaseConstant.FIREBASE_LOGIN_ERROR_MESSAGE
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
                painter = painterResource(id = R.drawable.ic_splash_screen),
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
                                contentDescription = OtherConstant.EMPTY_STRING,
                            )
                            Text(
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                text = stringResource(R.string.google)
                            )
                            Icon(
                                tint = Color.Transparent,
                                imageVector = Icons.Default.MailOutline,
                                contentDescription = OtherConstant.EMPTY_STRING,
                            )
                        }
                    )
                }
            )
        }
    }
}