package com.dirzaaulia.gamewish.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.dirzaaulia.gamewish.data.model.Wishlist
import com.dirzaaulia.gamewish.repository.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    val query = MutableLiveData<String>()

    val listWishlist = query.asFlow()
        .flatMapLatest {
            databaseRepository.getFilteredWishlist(it)
        }

    init {
        setSearchQuery("")
        addToWishlist(
            Wishlist(3328,
                "The Witcher 3: Wild Hunt",
                "https://media.rawg.io/media/games/618/618c2031a07bbff6b4f611f10b6bcdbc.jpg"
            )
        )
        addToWishlist(
            Wishlist(3498,
                "Grand Theft Auto V",
                "https://media.rawg.io/media/games/84d/84da2ac3fdfc6507807a1808595afb12.jpg"
            )
        )
    }

    fun setSearchQuery(searchQuery : String) {
        query.value = searchQuery
    }

    fun addToWishlist(wishlist: Wishlist) {
        viewModelScope.launch {
            databaseRepository.addToWishlist(wishlist)
        }
    }

    fun removeFromWishlist(wishlist: Wishlist) {
        viewModelScope.launch {
            databaseRepository.removeFromWishlist(wishlist)
        }
    }
}