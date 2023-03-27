package com.dirzaaulia.gamewish.ui.details

import androidx.annotation.MainThread
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dirzaaulia.gamewish.utils.ResponseResult
import com.dirzaaulia.gamewish.data.model.myanimelist.Details
import com.dirzaaulia.gamewish.data.model.myanimelist.ListStatus
import com.dirzaaulia.gamewish.data.model.rawg.GameDetails
import com.dirzaaulia.gamewish.data.model.tmdb.Image
import com.dirzaaulia.gamewish.data.model.tmdb.Movie
import com.dirzaaulia.gamewish.data.model.tmdb.MovieDetail
import com.dirzaaulia.gamewish.data.model.wishlist.GameWishlist
import com.dirzaaulia.gamewish.data.model.wishlist.MovieWishlist
import com.dirzaaulia.gamewish.data.response.rawg.ScreenshotsResponse
import com.dirzaaulia.gamewish.network.tmdb.paging.TmdbPagingSource
import com.dirzaaulia.gamewish.repository.DatabaseRepository
import com.dirzaaulia.gamewish.repository.FirebaseRepository
import com.dirzaaulia.gamewish.repository.MyAnimeListRepository
import com.dirzaaulia.gamewish.repository.ProtoRepository
import com.dirzaaulia.gamewish.repository.RawgRepository
import com.dirzaaulia.gamewish.repository.TmdbRepository
import com.dirzaaulia.gamewish.utils.OtherConstant
import com.dirzaaulia.gamewish.utils.TmdbConstant
import com.dirzaaulia.gamewish.utils.getTmdbRecomendations
import com.dirzaaulia.gamewish.utils.isSucceeded
import com.dirzaaulia.gamewish.utils.success
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    protoRepository: ProtoRepository,
    private val databaseRepository: DatabaseRepository,
    private val rawgRepository: RawgRepository,
    private val firebaseRepository: FirebaseRepository,
    private val myAnimeListRepository: MyAnimeListRepository,
    private val tmdbRepository: TmdbRepository,
) : ViewModel() {

    private val userPreferencesFlow = protoRepository.userPreferencesFlow

    val gameWishlistedData = mutableStateOf<GameWishlist?>(null)
    val movieWishlistedData = mutableStateOf<MovieWishlist?>(null)

    private var _token: MutableStateFlow<String> = MutableStateFlow(OtherConstant.EMPTY_STRING)

    private var _userAuthId: MutableStateFlow<String> = MutableStateFlow(OtherConstant.EMPTY_STRING)

    private val _gameDetailsResult: MutableStateFlow<ResponseResult<GameDetails>?> =
        MutableStateFlow(null)
    val gameDetailsResult = _gameDetailsResult.asStateFlow()

    private val _gameDetails: MutableStateFlow<GameDetails?> = MutableStateFlow(null)
    val gameDetails = _gameDetails.asStateFlow()

    private val _gameDetailsScreenshots: MutableStateFlow<ScreenshotsResponse?> =
        MutableStateFlow(null)
    val gameDetailsScreenshots = _gameDetailsScreenshots.asStateFlow()

    private var _updateGameResult: MutableStateFlow<ResponseResult<Task<Void>>?> =
        MutableStateFlow(null)
    val updateGameResult = _updateGameResult.asStateFlow()

    private var _deleteGameResult: MutableStateFlow<ResponseResult<Task<Void>>?>
    = MutableStateFlow(null)
    val deleteGameResult = _deleteGameResult.asStateFlow()

    private var _updateMovieResult: MutableStateFlow<ResponseResult<Task<Void>>?> =
        MutableStateFlow(null)
    val updateMovieResult = _updateMovieResult.asStateFlow()

    private var _deleteMovieResult: MutableStateFlow<ResponseResult<Task<Void>>?>
            = MutableStateFlow(null)
    val deleteMovieResult = _deleteMovieResult.asStateFlow()

    private val _animeDetailsResult: MutableStateFlow<ResponseResult<Details>?> =
        MutableStateFlow(null)
    val animeDetailsResult = _animeDetailsResult.asStateFlow()

    private val _animeDetails: MutableStateFlow<Details?> = MutableStateFlow(null)
    val animeDetails = _animeDetails.asStateFlow()

    private val _updateMyAnimeListResult: MutableStateFlow<ResponseResult<ListStatus>?>
            = MutableStateFlow(null)
    val updateMyAnimeListResult = _updateMyAnimeListResult.asStateFlow()

    private val _deleteMyAnimeListResult: MutableStateFlow<ResponseResult<Unit>?>
            = MutableStateFlow(null)
    val deleteMyAnimeListResult = _deleteMyAnimeListResult.asStateFlow()

    private val _movieDetailsResult: MutableStateFlow<ResponseResult<MovieDetail>?> =
        MutableStateFlow(null)
    val movieDetailsResult = _movieDetailsResult.asStateFlow()

    private val _movieDetails: MutableStateFlow<MovieDetail?> = MutableStateFlow(null)
    val movieDetails = _movieDetails.asStateFlow()

    private val _movieImages: MutableStateFlow<List<Image>?> = MutableStateFlow(null)
    val movieImages = _movieImages.asStateFlow()

    private val _selectedAnimeTab: MutableStateFlow<Int> = MutableStateFlow(OtherConstant.ZERO)
    val selectedAnimeTab = _selectedAnimeTab.asStateFlow()

    private val _selectedMovieTab: MutableStateFlow<Int> = MutableStateFlow(OtherConstant.ZERO)
    val selectedMovieTab = _selectedMovieTab.asStateFlow()

    private val movieType = MutableStateFlow(OtherConstant.EMPTY_STRING)

    private val movieId = MutableStateFlow(OtherConstant.ZERO_LONG)
    var movieRecommendations: Flow<PagingData<Movie>>? = null

    init {
        getUserAuthData()
    }

    @MainThread
    fun setMovieId(movieId: Long) {
        this.movieId.value = movieId
    }

    @MainThread
    fun setMovieType(type: String) {
        movieType.value = type
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
    fun setUpdateMyAnimeListResult() {
        _updateMyAnimeListResult.value = null
    }

    @MainThread
    fun setDeleteMyAnimeListResult() {
        _deleteMyAnimeListResult.value = null
    }

    private fun getUserAuthData() {
        viewModelScope.launch {
            userPreferencesFlow.collect { user ->
                if (user.accessToken.isNullOrBlank())
                    _token.value = OtherConstant.EMPTY_STRING
                else
                    _token.value = user.accessToken

                if (user.userAuthId.isNullOrBlank())
                    _userAuthId.value = OtherConstant.EMPTY_STRING
                else
                    _userAuthId.value = user.userAuthId
            }
        }
    }

    private fun addGameWishlistToFirestore(gameWishlist: GameWishlist) {
        firebaseRepository.addGameToWishlist(_userAuthId.value, gameWishlist)
            .onEach {
                if (it.isSucceeded) addGameToLocal(gameWishlist)
                _updateGameResult.value = it
            }
            .launchIn(viewModelScope)
    }

    private fun addMovieWishlistToFirestore(movieWishlist: MovieWishlist) {
        firebaseRepository.addMovieToWishlist(_userAuthId.value, movieWishlist)
            .onEach {
                 if (it.isSucceeded) {
                     addMovieToLocal(movieWishlist)
                     movieWishlist.id?.let { id -> checkIfMovieWishlisted(id) }
                 }
                _updateMovieResult.value = it
            }
            .launchIn(viewModelScope)
    }

    private fun deleteGameWishlistFromFirestore(gameWishlist: GameWishlist) {
        firebaseRepository.removeGameFromWishlist(_userAuthId.value, gameWishlist)
            .onEach {
                if (it.isSucceeded) deleteGameFromLocal(gameWishlist)
                _deleteGameResult.value = it
            }
            .launchIn(viewModelScope)
    }

    private fun deleteMovieWishlistFromFirestore(movieWishlist: MovieWishlist) {
        firebaseRepository.removeMovieFromWishlist(_userAuthId.value, movieWishlist)
            .onEach {
                if (it.isSucceeded) deleteMovieFromLocal(movieWishlist)
                _deleteMovieResult.value = it
            }
            .launchIn(viewModelScope)
    }

    private fun addGameToLocal(gameWishlist: GameWishlist) {
        viewModelScope.launch {
            databaseRepository.addToGameWishlist(gameWishlist)
        }
    }

    private fun deleteGameFromLocal(gameWishlist: GameWishlist) {
        viewModelScope.launch {
            databaseRepository.deleteGameWishlist(gameWishlist)
        }
    }

    private fun addMovieToLocal(movieWishlist: MovieWishlist) {
        viewModelScope.launch {
            databaseRepository.addToMovieWishlist(movieWishlist)
        }
    }

    private fun deleteMovieFromLocal(movieWishlist: MovieWishlist) {
        viewModelScope.launch {
            databaseRepository.deleteMovieWishlist(movieWishlist)
        }
    }

    private fun getTv(movieId: Long) {
        tmdbRepository.getTVDetails(movieId)
            .onEach {
                it.success { data ->
                    _movieDetails.value = data
                }
                _movieDetailsResult.value = it
            }
            .launchIn(viewModelScope)
    }

    private fun getMovie(movieId: Long) {
        tmdbRepository.getMovieDetails(movieId)
            .onEach {
                it.success { data ->
                    _movieDetails.value = data
                }
                _movieDetailsResult.value = it
            }
            .launchIn(viewModelScope)
    }

    private fun getImagesTv(movieId: Long) {
        tmdbRepository.getTVImages(movieId)
            .onEach {
                it.success { data ->
                    _movieImages.value = data.imageList
                }
            }
            .launchIn(viewModelScope)
    }

    private fun getImagesMovie(movieId: Long) {
        tmdbRepository.getMovieImages(movieId)
            .onEach {
                it.success { data ->
                    _movieImages.value = data.imageList
                }
            }
            .launchIn(viewModelScope)
    }

    fun getGameDetails(gameId: Long) {
        rawgRepository.getGameDetails(gameId)
            .onEach {
                it.success { data ->
                    _gameDetails.value = data
                }
                _gameDetailsResult.value = it
            }
            .launchIn(viewModelScope)
    }

    fun getGameDetailsScreenshots(gameId: Long) {
        rawgRepository.getGameDetailsScreenshots(gameId)
            .onEach {
                it.success { data ->
                    _gameDetailsScreenshots.value = data
                }
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
            }
            .launchIn(viewModelScope)
    }

    fun getMovieDetails(movieId: Long) {
        if (movieType.value.equals(TmdbConstant.TMDB_TYPE_MOVIE, true)) 
            getMovie(movieId) else getTv(movieId)
    }

    fun getMovieImages(movieId: Long) {
        if (movieType.value.equals(TmdbConstant.TMDB_TYPE_MOVIE, true)) {
            getImagesMovie(movieId)
        } else {
            getImagesTv(movieId)
        }
    }

    fun checkIfGameWishlisted(gameId: Long) {
        viewModelScope.launch {
            databaseRepository.getGameWishlist(gameId).collect {
                gameWishlistedData.value = it
            }
        }
    }

    fun checkIfMovieWishlisted(id: Long) {
        viewModelScope.launch {
            if (movieType.value.equals(TmdbConstant.TMDB_TYPE_MOVIE, true)) {
                databaseRepository.getMovieWishlist(id).collect {
                    movieWishlistedData.value = it
                }
            } else {
                databaseRepository.getTVShowWishlist(id).collect {
                    movieWishlistedData.value = it
                }
            }
        }
    }

    fun addToGameWishlist(gameWishlist: GameWishlist) {
        viewModelScope.launch {
            addGameWishlistToFirestore(gameWishlist)
        }
    }

    fun deleteGameWishlist(gameWishlist: GameWishlist) {
        viewModelScope.launch {
            deleteGameWishlistFromFirestore(gameWishlist)
        }
    }

    fun addToMovieWishlist(movieWishlist: MovieWishlist) {
        viewModelScope.launch {
            addMovieWishlistToFirestore(movieWishlist)
        }
    }

    fun deleteMovieWishlist(movieWishlist: MovieWishlist) {
        viewModelScope.launch {
            deleteMovieWishlistFromFirestore(movieWishlist)
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
            accessToken = _token.value,
            animeId = animeId,
            status = status,
            isRewatching = isRewatching,
            score = score,
            numberWatched = numberWatched
        ).onEach {
            _updateMyAnimeListResult.value = it
        }.launchIn(viewModelScope)
    }

    fun deleteMyAnimeListAnimeList(animeId: Long) {
        myAnimeListRepository.deleteMyAnimeListAnimeList(_token.value, animeId)
            .onEach { result -> _deleteMyAnimeListResult.value = result }
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
            .onEach { response -> _deleteMyAnimeListResult.value = response }
            .launchIn(viewModelScope)
    }

    fun getMovieRecommendations() {
        val serviceCode = movieType.value.getTmdbRecomendations()
        movieRecommendations = movieId.flatMapLatest { movieId ->
            Pager(PagingConfig(pageSize = TmdbConstant.TMDB_PAGE_SIZE_TEN)) {
                TmdbPagingSource(
                    repository = tmdbRepository,
                    searchQuery = OtherConstant.EMPTY_STRING,
                    movieId = movieId,
                    serviceCode = serviceCode
                )
            }.flow.cachedIn(viewModelScope)
        }
    }
}