package com.dirzaaulia.gamewish.ui.main.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirzaaulia.gamewish.repository.FirebaseRepository
import com.dirzaaulia.gamewish.repository.ProtoRepository
import com.dirzaaulia.gamewish.utils.FirebaseState
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val protoRepository: ProtoRepository
) : ViewModel() {

    val loadingState = MutableStateFlow(FirebaseState.IDLE)
    val userAuthId = mutableStateOf("")

    private val userPreferencesFlow = protoRepository.userPreferencesFlow

    private val _googleLoginStatus = MutableLiveData<Boolean>()
    val googleLoginStatus : LiveData<Boolean>
        get() = _googleLoginStatus

    init {
        getUserAuthStatus()
    }

    private fun getUserAuthStatus() {
        viewModelScope.launch {
            userPreferencesFlow.collect {
                userAuthId.value = it.userAuthId
            }
        }
    }

    fun setUserAuthId(uid : String) {
        viewModelScope.launch {
            protoRepository.updateUserAuthId(uid)
        }
    }

    fun getFirebaseAuth(): FirebaseAuth {
        return firebaseRepository.getFirebaseAuth()
    }

    fun authGoogleLogin(idToken: String): AuthCredential {
        return firebaseRepository.authGoogleLogin(idToken)
    }

    fun signWithCredential(credential: AuthCredential) = viewModelScope.launch {
        try {
            loadingState.emit(FirebaseState.LOADING)
            firebaseRepository.signIn(credential)
            loadingState.emit(FirebaseState.LOADED)
        } catch (e: Exception) {
            loadingState.emit(FirebaseState.error(e.localizedMessage))
        }
    }

    fun getGoogleLoginStatus() {
        val status = firebaseRepository.getGoogleLoginStatus()
        _googleLoginStatus.value = status
    }

    fun setLocalDataStatus(status : Boolean) {
        viewModelScope.launch(Dispatchers.Main) {
            protoRepository.updateLocalDataStatus(status)
        }
    }

}