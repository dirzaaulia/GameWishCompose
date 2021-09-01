package com.dirzaaulia.gamewish.ui.home

import androidx.annotation.MainThread
import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import com.dirzaaulia.gamewish.repository.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    private val _selectedBottomNav : MutableStateFlow<Int> = MutableStateFlow(0)
    val selectedBottomNav : StateFlow<Int> get() = _selectedBottomNav.asStateFlow()

    val query = MutableLiveData<String>()

    val listWishlist = query.asFlow()
        .flatMapLatest {
            databaseRepository.getFilteredWishlist(it)
        }

    @MainThread
    fun selectBottomNavMenu(@StringRes tab: Int) {
        _selectedBottomNav.value = tab
    }
}