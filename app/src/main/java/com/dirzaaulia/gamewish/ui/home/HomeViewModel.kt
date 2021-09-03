package com.dirzaaulia.gamewish.ui.home

import androidx.annotation.MainThread
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirzaaulia.gamewish.data.model.Wishlist
import com.dirzaaulia.gamewish.repository.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    private val _selectedBottomNav: MutableStateFlow<Int> = MutableStateFlow(0)
    val selectedBottomNav: StateFlow<Int> get() = _selectedBottomNav.asStateFlow()

    private val _selectedWishlistTab: MutableStateFlow<Int> = MutableStateFlow(0)
    val selectedWishlistTab: StateFlow<Int> get() = _selectedWishlistTab.asStateFlow()

    val query = MutableStateFlow("")

    val listWishlist = query
        .flatMapLatest {
            databaseRepository.getFilteredWishlist(it)
        }

    init {
        addToWishlist(Wishlist.mock())
    }

    @MainThread
    fun setSearchQuery(searchQuery: String) {
        query.value = searchQuery
    }

    @MainThread
    fun selectBottomNavMenu(@StringRes tab: Int) {
        _selectedBottomNav.value = tab
    }

    @MainThread
    fun selectWishlistTab(tabIndex: Int) {
        _selectedWishlistTab.value = tabIndex
    }

    fun addToWishlist(wishlist: Wishlist) {
        viewModelScope.launch {
            databaseRepository.addToWishlist(wishlist)
        }
    }
}