package com.dirzaaulia.gamewish.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _isShowSnackbar : MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isShowSnackbar : StateFlow<Boolean> get() = _isShowSnackbar.asStateFlow()

    fun toogleSnackbar() {
        _isShowSnackbar.tryEmit(true)
        viewModelScope.launch {
            delay(1500)
            _isShowSnackbar.tryEmit(false)
        }
    }
}