package com.dirzaaulia.gamewish.ui.details

import androidx.annotation.MainThread
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirzaaulia.gamewish.base.ResponseResult
import com.dirzaaulia.gamewish.data.model.Wishlist
import com.dirzaaulia.gamewish.data.model.myanimelist.Details
import com.dirzaaulia.gamewish.data.model.rawg.GameDetails
import com.dirzaaulia.gamewish.data.response.rawg.ScreenshotsResponse
import com.dirzaaulia.gamewish.extension.success
import com.dirzaaulia.gamewish.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val rawgRepository: RawgRepository,
    private val firebaseRepository: FirebaseRepository,
    private val myAnimeListRepository: MyAnimeListRepository,
    private val protoRepository: ProtoRepository,
) : ViewModel() {

    private val userPreferencesFlow = protoRepository.userPreferencesFlow

    val loading = mutableStateOf(true)
    val wishlistedData = mutableStateOf<Wishlist?>(null)

    private var _token: MutableStateFlow<String> = MutableStateFlow("")
    val token = _token.asStateFlow()


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

    private val _animeDetailsResult: MutableStateFlow<ResponseResult<Details>?> =
        MutableStateFlow(null)
    val animeDetailsResult = _animeDetailsResult.asStateFlow()

    private val _animeDetails: MutableStateFlow<Details?> = MutableStateFlow(null)
    val animeDetails = _animeDetails.asStateFlow()

    private val _selectedAnimeTab: MutableStateFlow<Int> = MutableStateFlow(0)
    val selectedAnimeTab = _selectedAnimeTab.asStateFlow()

    init {
        getAccessToken()
    }

    @MainThread
    fun selectAnimeDetailsTab(tab: Int) {
        _selectedAnimeTab.value = tab
    }

    fun getAccessToken() {
        viewModelScope.launch {
            userPreferencesFlow.collect {
                if (it.accessToken.isNullOrBlank()) {
                    _token.value = ""
                } else {
                    _token.value = it.accessToken
                }
            }
        }
    }

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

    fun getAnimeDetails(animeId: Long) {
        myAnimeListRepository.getMyAnimeListAnimeDetails(_token.value, animeId)
            .onEach {
                it.success { data ->
                    _animeDetails.value = data
                }
                _animeDetailsResult.value = it
                loading.value = false
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