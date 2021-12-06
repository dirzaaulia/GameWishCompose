package com.dirzaaulia.gamewish.ui.details

import android.media.ImageReader
import androidx.annotation.MainThread
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.dirzaaulia.gamewish.base.ResponseResult
import com.dirzaaulia.gamewish.data.model.myanimelist.Details
import com.dirzaaulia.gamewish.data.model.myanimelist.ListStatus
import com.dirzaaulia.gamewish.data.model.rawg.GameDetails
import com.dirzaaulia.gamewish.data.model.tmdb.Image
import com.dirzaaulia.gamewish.data.model.tmdb.MovieDetail
import com.dirzaaulia.gamewish.data.model.wishlist.GameWishlist
import com.dirzaaulia.gamewish.data.response.rawg.ScreenshotsResponse
import com.dirzaaulia.gamewish.data.response.tmdb.ImagesResponse
import com.dirzaaulia.gamewish.extension.success
import com.dirzaaulia.gamewish.network.rawg.paging.RawgSearchPagingSource
import com.dirzaaulia.gamewish.network.tmdb.paging.TmdbPagingSource
import com.dirzaaulia.gamewish.repository.*
import com.google.android.gms.tasks.Task
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
    private val tmdbRepository: TmdbRepository,
) : ViewModel() {

    private val userPreferencesFlow = protoRepository.userPreferencesFlow

    val loading = mutableStateOf(true)
    val wishlistedData = mutableStateOf<GameWishlist?>(null)

    private var _token: MutableStateFlow<String> = MutableStateFlow("")
    val token = _token.asStateFlow()

    private var _userAuthId: MutableStateFlow<String> = MutableStateFlow("")
    val userAuthId = _userAuthId.asStateFlow()

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

    private var _updateGameResult: MutableStateFlow<ResponseResult<Void>?> = MutableStateFlow(null)
    val updateGameResult = _updateGameResult.asStateFlow()

    private var _deleteGameResult: MutableStateFlow<String> = MutableStateFlow("")
    val deleteGameResult = _deleteGameResult.asStateFlow()

    private val _animeDetailsResult: MutableStateFlow<ResponseResult<Details>?> =
        MutableStateFlow(null)
    val animeDetailsResult = _animeDetailsResult.asStateFlow()

    private val _animeDetails: MutableStateFlow<Details?> = MutableStateFlow(null)
    val animeDetails = _animeDetails.asStateFlow()

    private val _updateMyAnimeListResult: MutableStateFlow<ResponseResult<ListStatus>?>
            = MutableStateFlow(null)
    val updateMyAnimeListResult = _updateMyAnimeListResult.asStateFlow()

    private val _deleteMyAnimeListResult: MutableStateFlow<ResponseResult<String>?>
            = MutableStateFlow(null)
    val deleteMyAnimeListResult = _deleteMyAnimeListResult.asStateFlow()

    private val _movieDetailsResult: MutableStateFlow<ResponseResult<MovieDetail>?> =
        MutableStateFlow(null)
    val movieDetailsResult = _movieDetailsResult.asStateFlow()

    private val _movieDetails: MutableStateFlow<MovieDetail?> = MutableStateFlow(null)
    val movieDetails = _movieDetails.asStateFlow()

    private val _movieImagesResult: MutableStateFlow<ResponseResult<ImagesResponse>?> =
        MutableStateFlow(null)
    val movieImagesResult = _movieImagesResult.asStateFlow()

    private val _movieImages: MutableStateFlow<List<Image>?> = MutableStateFlow(null)
    val movieImages = _movieImages.asStateFlow()

    private val _selectedAnimeTab: MutableStateFlow<Int> = MutableStateFlow(0)
    val selectedAnimeTab = _selectedAnimeTab.asStateFlow()

    private val _selectedMovieTab: MutableStateFlow<Int> = MutableStateFlow(0)
    val selectedMovieTab = _selectedMovieTab.asStateFlow()

    private val movieId = MutableStateFlow(0L)
    val movieRecommendations = movieId.flatMapLatest {
        Pager(PagingConfig(pageSize = 10)) {
            TmdbPagingSource(
                tmdbRepository,
                "",
                it,
                3
            )
        }.flow.cachedIn(viewModelScope)
    }

    init {
        getUserAuthData()
    }

    @MainThread
    fun setMovieId(movieId: Long) {
        this.movieId.value = movieId
    }

    @MainThread
    fun selectAnimeDetailsTab(tab: Int) {
        _selectedAnimeTab.value = tab
    }

    @MainThread
    fun selectMovieDetailsTab(tab: Int) {
        _selectedMovieTab.value = tab
    }

    @MainThread
    fun setLoading(status: Boolean) {
        loading.value = status
    }

    @MainThread
    fun setUpdateMyAnimeListResult() {
        _updateMyAnimeListResult.value = null
    }

    @MainThread
    fun setDeleteMyAnimeListResult() {
        _deleteMyAnimeListResult.value = null
    }

    private fun getUserAuthData() {
        viewModelScope.launch {
            userPreferencesFlow.collect {
                if (it.accessToken.isNullOrBlank()) {
                    _token.value = ""
                } else {
                    _token.value = it.accessToken
                }

                if (it.userAuthId.isNullOrBlank()) {
                    _userAuthId.value = ""
                } else {
                    _userAuthId.value = it.userAuthId
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

    fun getMangaDetails(animeId: Long) {
        myAnimeListRepository.getMyAnimeListMangaDetails(_token.value, animeId)
            .onEach {
                it.success { data ->
                    _animeDetails.value = data
                }
                _animeDetailsResult.value = it
                loading.value = false
            }
            .launchIn(viewModelScope)
    }

    fun getMovieDetails(movieId: Long) {
        tmdbRepository.getMovieDetails(movieId)
            .onEach {
                it.success { data ->
                    _movieDetails.value = data
                }
                _movieDetailsResult.value = it
                loading.value = false
            }
            .launchIn(viewModelScope)
    }

    fun getMovieImages(movieId: Long) {
        tmdbRepository.getMovieImages(movieId)
            .onEach {
                it.success { data ->
                    _movieImages.value = data.imageList
                }
                _movieImagesResult.value = it
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

    fun addWishlistToFirestore(gameWishlist: GameWishlist) {
       firebaseRepository.addGameToWishlist(_userAuthId.value, gameWishlist)
           .onEach {
               _updateGameResult.value = it
           }
           .launchIn(viewModelScope)
    }

    fun deleteWishlistFromFirestore(gameWishlist: GameWishlist): Task<Void> {
        return firebaseRepository.removeGameFromWishlist(_userAuthId.value, gameWishlist)
    }

    fun addToWishlist(gameWishlist: GameWishlist) {
        viewModelScope.launch {
            databaseRepository.addToWishlist(gameWishlist)
        }
    }

    fun deleteWishlist(gameWishlist: GameWishlist) {
        viewModelScope.launch {
            databaseRepository.deleteWishlist(gameWishlist)
        }
    }

    fun updateMyAnimeListAnimeList(
        animeId: Long,
        status: String,
        isRewatching: Boolean,
        score: Int,
        numberWatched: Int
    ) {
        myAnimeListRepository.updateMyAnimeListAnimeList(
            _token.value,
            animeId,
            status,
            isRewatching,
            score,
            numberWatched
        ).onEach {
            _updateMyAnimeListResult.value = it
        }.launchIn(viewModelScope)
    }

    fun deleteMyAnimeListAnimeList(animeId: Long) {
        myAnimeListRepository.deleteMyAnimeListAnimeList(_token.value, animeId)
            .onEach {
                _deleteMyAnimeListResult.value = it
            }
            .launchIn(viewModelScope)
    }

    fun updateMyAnimeListMangaList(
        mangaId: Long,
        status: String,
        isRereading: Boolean,
        score: Int,
        numberRead: Int
    ) {
        myAnimeListRepository.updateMyAnimeListMangaList(
            _token.value,
            mangaId,
            status,
            isRereading,
            score,
            numberRead
        ).onEach {
            _updateMyAnimeListResult.value = it
        }.launchIn(viewModelScope)
    }

    fun deleteMyAnimeListMangaList(mangaId: Long) {
        myAnimeListRepository.deleteMyAnimeListMangaList(_token.value, mangaId)
            .onEach {
                _deleteMyAnimeListResult.value = it
            }
            .launchIn(viewModelScope)
    }
}