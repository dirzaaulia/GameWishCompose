package com.dirzaaulia.gamewish.ui.search

import androidx.annotation.MainThread
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.dirzaaulia.gamewish.base.ResponseResult
import com.dirzaaulia.gamewish.data.model.myanimelist.ServiceCode
import com.dirzaaulia.gamewish.data.request.myanimelist.SearchGameRequest
import com.dirzaaulia.gamewish.network.myanimelist.paging.MyAnimeListPagingSource
import com.dirzaaulia.gamewish.network.rawg.paging.RawgGenrePagingSource
import com.dirzaaulia.gamewish.network.rawg.paging.RawgPlatformPagingSource
import com.dirzaaulia.gamewish.network.rawg.paging.RawgPublisherPagingSource
import com.dirzaaulia.gamewish.network.rawg.paging.RawgSearchPagingSource
import com.dirzaaulia.gamewish.network.tmdb.paging.TmdbPagingSource
import com.dirzaaulia.gamewish.repository.MyAnimeListRepository
import com.dirzaaulia.gamewish.repository.ProtoRepository
import com.dirzaaulia.gamewish.repository.RawgRepository
import com.dirzaaulia.gamewish.repository.TmdbRepository
import com.dirzaaulia.gamewish.utils.OtherConstant
import com.dirzaaulia.gamewish.utils.RawgConstant
import com.dirzaaulia.gamewish.utils.TmdbConstant
import com.dirzaaulia.gamewish.utils.getAnimeSeason
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.dirzaaulia.gamewish.data.model.tmdb.ServiceCode as TmdbCode

@HiltViewModel
class SearchViewModel @Inject constructor(
    protoRepository: ProtoRepository,
    private val rawgRepository: RawgRepository,
    private val myAnimeListRepository: MyAnimeListRepository,
    private val tmdbRepository: TmdbRepository,
) : ViewModel() {

    private val userPreferencesFlow = protoRepository.userPreferencesFlow

    private val _selectedBottomNav: MutableStateFlow<Int> = MutableStateFlow(OtherConstant.ZERO)
    val selectedBottomNav = _selectedBottomNav.asStateFlow()

    private val _selectedSearchGameTab: MutableStateFlow<Int> = MutableStateFlow(OtherConstant.ZERO)
    val selectedSearchGameTab = _selectedSearchGameTab.asStateFlow()

    private val _selectedSearchAnimeTab: MutableStateFlow<Int> = MutableStateFlow(OtherConstant.ZERO)
    val selectedSearchAnimeTab = _selectedSearchAnimeTab.asStateFlow()

    private val _selectedSearchMovieTab: MutableStateFlow<Int> = MutableStateFlow(OtherConstant.ZERO)
    val selectedSearchMovieTab = _selectedSearchMovieTab.asStateFlow()

    private var _tokenResult: MutableStateFlow<ResponseResult<String>?> = MutableStateFlow(null)

    private var _token: MutableStateFlow<String> = MutableStateFlow(OtherConstant.EMPTY_STRING)

    private val _refreshToken: MutableStateFlow<String> = MutableStateFlow(OtherConstant.EMPTY_STRING)

    val genres =  Pager(PagingConfig(pageSize = RawgConstant.RAWG_PAGE_SIZE_TEN)) {
        RawgGenrePagingSource(rawgRepository)
    }.flow.cachedIn(viewModelScope)

    val publishers =  Pager(PagingConfig(pageSize = RawgConstant.RAWG_PAGE_SIZE_TEN)) {
        RawgPublisherPagingSource(rawgRepository)
    }.flow.cachedIn(viewModelScope)

    val platforms = Pager(PagingConfig(pageSize = RawgConstant.RAWG_PAGE_SIZE_TEN)) {
        RawgPlatformPagingSource(rawgRepository)
    }.flow.cachedIn(viewModelScope)

    val searchGameRequest = MutableStateFlow(SearchGameRequest.default())

    val searchGameList = searchGameRequest.flatMapLatest { request ->
        Pager(PagingConfig(pageSize = RawgConstant.RAWG_PAGE_SIZE_FIFTY)) {
            RawgSearchPagingSource(
                repository = rawgRepository,
                query = request.searchQuery,
                genreId = request.genreId,
                publisherId = request.publisherId,
                platformId = request.platformId
            )
        }.flow.cachedIn(viewModelScope)
    }

    val searchAnimeQuery = MutableStateFlow(OtherConstant.EMPTY_STRING)
    val searchAnimeList = _token
        .flatMapLatest { token ->
            searchAnimeQuery.flatMapLatest { query ->
                Pager(PagingConfig(pageSize = RawgConstant.RAWG_PAGE_SIZE_TEN)) {
                    MyAnimeListPagingSource(
                        repository = myAnimeListRepository,
                        serviceCode = ServiceCode.SEARCH_ANIME,
                        accessToken = token,
                        listStatus = OtherConstant.EMPTY_STRING,
                        seasonalQuery = OtherConstant.EMPTY_STRING,
                        searchQuery = query
                    )
                }.flow.cachedIn(viewModelScope)
            }
        }
    val searchMangaList = _token
        .flatMapLatest { token ->
            searchAnimeQuery.flatMapLatest { query ->
                Pager(PagingConfig(pageSize = RawgConstant.RAWG_PAGE_SIZE_TEN)) {
                    MyAnimeListPagingSource(
                        repository = myAnimeListRepository,
                        serviceCode = ServiceCode.SEARCH_MANGA,
                        accessToken = token,
                        listStatus = OtherConstant.EMPTY_STRING,
                        seasonalQuery = OtherConstant.EMPTY_STRING,
                        searchQuery = query
                    )
                }.flow.cachedIn(viewModelScope)
            }
        }

    val seasonalAnimeQuery = MutableStateFlow(getAnimeSeason())
    val seasonalAnimeList = _token
        .flatMapLatest { token ->
            seasonalAnimeQuery.flatMapLatest { query ->
                Pager(PagingConfig(pageSize = RawgConstant.RAWG_PAGE_SIZE_TEN)) {
                    MyAnimeListPagingSource(
                        repository = myAnimeListRepository,
                        serviceCode = ServiceCode.SEASONAL_ANIME,
                        accessToken = token,
                        listStatus = OtherConstant.EMPTY_STRING,
                        seasonalQuery = query,
                        searchQuery = OtherConstant.EMPTY_STRING
                    )
                }.flow.cachedIn(viewModelScope)
            }
        }

    val searchMovieQuery = MutableStateFlow(OtherConstant.EMPTY_STRING)
    val searchMovieList = searchMovieQuery.flatMapLatest { query ->
            Pager(PagingConfig(pageSize = TmdbConstant.TMDB_PAGE_SIZE_TEN)) {
                TmdbPagingSource(
                    repository = tmdbRepository,
                    searchQuery = query,
                    movieId = OtherConstant.ZERO_LONG,
                    serviceCode = TmdbCode.SEARCH_MOVIE
                )
            }.flow.cachedIn(viewModelScope)
        }

    val searchTvList = searchMovieQuery.flatMapLatest { query ->
            Pager(PagingConfig(pageSize = TmdbConstant.TMDB_PAGE_SIZE_TEN)) {
                TmdbPagingSource(
                    repository = tmdbRepository,
                    searchQuery = query,
                    movieId = OtherConstant.ZERO_LONG,
                    serviceCode = TmdbCode.SEARCH_TV
                )
            }.flow.cachedIn(viewModelScope)
        }

    init {
        getAccessToken()
    }

    @MainThread
    fun selectBottomNavMenu(@StringRes tab: Int) {
        _selectedBottomNav.value = tab
    }

    @MainThread
    fun selectSearchGameTab(tabIndex: Int) {
        _selectedSearchGameTab.value = tabIndex
    }

    @MainThread
    fun selectSearchAnimeTab(tabIndex: Int) {
        _selectedSearchAnimeTab.value = tabIndex
    }

    @MainThread
    fun selectSearchMovieTab(tabIndex: Int) {
        _selectedSearchMovieTab.value = tabIndex
    }

    @MainThread
    fun setSearchGameRequest(request: SearchGameRequest) {
        searchGameRequest.value = request
    }

    @MainThread
    fun setSearchAnimeQuery(query: String) {
        searchAnimeQuery.value = query
    }

    @MainThread
    fun setSearchSeasonalQuery(query: String) {
        seasonalAnimeQuery.value = query
    }

    @MainThread
    fun setSearchMovieQuery(query: String) {
        searchMovieQuery.value = query
    }

    fun getAccessToken() {
        viewModelScope.launch {
            userPreferencesFlow.collect {
                if (it.accessToken.isNullOrBlank()) {
                    _tokenResult.value = ResponseResult.Error(Exception())
                    _token.value = OtherConstant.EMPTY_STRING
                    _refreshToken.value = OtherConstant.EMPTY_STRING
                } else {
                    _tokenResult.value = ResponseResult.Success(it.accessToken)
                    _token.value = it.accessToken
                    _refreshToken.value = it.refreshToken
                }
            }
        }
    }
}