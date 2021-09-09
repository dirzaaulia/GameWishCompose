package com.dirzaaulia.gamewish.ui.details

import androidx.annotation.MainThread
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirzaaulia.gamewish.base.ResponseResult
import com.dirzaaulia.gamewish.data.model.Wishlist
import com.dirzaaulia.gamewish.data.model.rawg.GameDetails
import com.dirzaaulia.gamewish.data.response.rawg.ScreenshotsResponse
import com.dirzaaulia.gamewish.extension.success
import com.dirzaaulia.gamewish.repository.DatabaseRepository
import com.dirzaaulia.gamewish.repository.RawgRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val rawgRepository: RawgRepository
) : ViewModel() {

    val loading = mutableStateOf(true)
    val wishlistedData = mutableStateOf<Wishlist?>(null)

    private val _gameDetailsResult: MutableStateFlow<ResponseResult<GameDetails>?> =
        MutableStateFlow(null)
    val gameDetailsResult = _gameDetailsResult.asStateFlow()

    private val _gameDetails: MutableStateFlow<GameDetails?> = MutableStateFlow(null)
    val gameDetails = _gameDetails.asStateFlow()

    private val _gameDetailsScreenshotsResult: MutableStateFlow<ResponseResult<ScreenshotsResponse>?> =
        MutableStateFlow(null)
    val gameDetailsScreenshotsResult = _gameDetailsScreenshotsResult.asStateFlow()

    private val _gameDetailsScreenshots: MutableStateFlow<ScreenshotsResponse?> =
        MutableStateFlow(null)
    val gameDetailsScreenshots = _gameDetailsScreenshots.asStateFlow()

    fun getGameDetails(gameId: Long) {
        rawgRepository.getGameDetails(gameId)
            .onEach {
                it.success { data ->
                    _gameDetails.value = data
                }
                _gameDetailsResult.value = it
                loading.value = false
            }
            .launchIn(viewModelScope)
    }

    fun getGameDetailsScreenshots(gameId: Long) {
        rawgRepository.getGameDetailsScreenshots(gameId)
            .onEach {
                it.success { data ->
                    _gameDetailsScreenshots.value = data
                }
                _gameDetailsScreenshotsResult.value = it
            }
            .launchIn(viewModelScope)
    }

    fun checkIfGameWishlisted(gameId: Long) {
        viewModelScope.launch {
            databaseRepository.getWishlist(gameId).collect {
                wishlistedData.value = it
            }
        }
    }

    fun addToWishlist(wishlist: Wishlist) {
        viewModelScope.launch {
            databaseRepository.addToWishlist(wishlist)
        }
    }

    fun deleteWishlist(wishlist: Wishlist) {
        viewModelScope.launch {
            databaseRepository.deleteWishlist(wishlist)
        }
    }

    @MainThread
    fun setLoading(status: Boolean) {
        loading.value = status
    }
}