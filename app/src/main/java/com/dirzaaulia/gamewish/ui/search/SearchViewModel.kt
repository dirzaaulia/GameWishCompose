package com.dirzaaulia.gamewish.ui.search

import androidx.annotation.MainThread
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.dirzaaulia.gamewish.base.ResponseResult
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val rawgRepository: RawgRepository,
    private val protoRepository: ProtoRepository,
    private val myAnimeListRepository: MyAnimeListRepository,
    private val tmdbRepository: TmdbRepository,
) : ViewModel() {

    private val userPreferencesFlow = protoRepository.userPreferencesFlow

    private val _selectedBottomNav: MutableStateFlow<Int> = MutableStateFlow(0)
    val selectedBottomNav: StateFlow<Int> get() = _selectedBottomNav.asStateFlow()

    private val _selectedSearchGameTab: MutableStateFlow<Int> = MutableStateFlow(0)
    val selectedSearchGameTab: StateFlow<Int> get() = _selectedSearchGameTab.asStateFlow()

    private val _selectedSearchAnimeTab: MutableStateFlow<Int> = MutableStateFlow(0)
    val selectedSearchAnimeTab: StateFlow<Int> get() = _selectedSearchAnimeTab.asStateFlow()

    private val _selectedSearchMovieTab: MutableStateFlow<Int> = MutableStateFlow(0)
    val selectedSearchMovieTab: StateFlow<Int> get() = _selectedSearchMovieTab.asStateFlow()

    private var _tokenResult: MutableStateFlow<ResponseResult<String>?> = MutableStateFlow(null)
    val tokenResult = _tokenResult.asStateFlow()

    private var _token: MutableStateFlow<String> = MutableStateFlow("")
    val token = _token.asStateFlow()

    private val _refreshToken: MutableStateFlow<String> = MutableStateFlow("")
    val refreshToken = _refreshToken.asStateFlow()

    val genres =  Pager(PagingConfig(pageSize = 10)) {
        RawgGenrePagingSource(rawgRepository)
    }.flow.cachedIn(viewModelScope)

    val publishers =  Pager(PagingConfig(pageSize = 10)) {
        RawgPublisherPagingSource(rawgRepository)
    }.flow.cachedIn(viewModelScope)

    val platforms = Pager(PagingConfig(pageSize = 10)) {
        RawgPlatformPagingSource(rawgRepository)
    }.flow.cachedIn(viewModelScope)

    val searchGameRequest = MutableStateFlow(
        SearchGameRequest("", null, null, null)
    )
    val searchGameList = searchGameRequest
        .flatMapLatest {
        Pager(PagingConfig(pageSize = 10)) {
            RawgSearchPagingSource(
                rawgRepository,
                it.searchQuery,
                it.genreId,
                it.publisherId,
                it.platformId
            )
        }.flow.cachedIn(viewModelScope)
    }

    val searchAnimeQuery = MutableStateFlow("")
    val searchAnimeList = _token
        .flatMapLatest { token ->
            searchAnimeQuery.flatMapLatest {
                Pager(PagingConfig(pageSize = 10)) {
                    MyAnimeListPagingSource(
                        myAnimeListRepository,
                        3,
                        token,
                        "",
                        "",
                        it
                    )
                }.flow.cachedIn(viewModelScope)
            }
        }
    val searchMangaList = _token
        .flatMapLatest { token ->
            searchAnimeQuery.flatMapLatest {
                Pager(PagingConfig(pageSize = 10)) {
                    MyAnimeListPagingSource(
                        myAnimeListRepository,
                        4,
                        token,
                        "",
                        "",
                        it
                    )
                }.flow.cachedIn(viewModelScope)
            }
        }

    val seasonalAnimeQuery = MutableStateFlow("")
    val seasonalAnimeList = _token
        .flatMapLatest { token ->
            seasonalAnimeQuery.flatMapLatest {
                Pager(PagingConfig(pageSize = 10)) {
                    MyAnimeListPagingSource(
                        myAnimeListRepository,
                        2,
                        token,
                        "",
                        it,
                        ""
                    )
                }.flow.cachedIn(viewModelScope)
            }
        }

    val searchMovieQuery = MutableStateFlow("")
    val searchMovieList = searchMovieQuery
        .flatMapLatest {
            Pager(PagingConfig(pageSize = 10)) {
                TmdbPagingSource(tmdbRepository, it, 0, 1)
            }.flow.cachedIn(viewModelScope)
        }

    val searchTvList = searchMovieQuery
        .flatMapLatest {
            Pager(PagingConfig(pageSize = 10)) {
                TmdbPagingSource(tmdbRepository, it, 0, 2)
            }.flow.cachedIn(viewModelScope)
        }

    init {
        getAccessToken()
        getSeasonalAnimeQuery()
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

    @MainThread
    fun getSeasonalAnimeQuery() {
        val calendar = Calendar.getInstance()

        val setYear = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        var setSeason = ""

        when (month) {
            in 0..2 -> {
                setSeason = "winter"
            }
            in 3..5 -> {
                setSeason = "spring"
            }
            in 6..8 -> {
                setSeason = "summer"
            }
            in 9..11 -> {
                setSeason = "fall"
            }
        }

        seasonalAnimeQuery.value = "$setSeason $setYear"
    }

    fun getAccessToken() {
        viewModelScope.launch {
            userPreferencesFlow.collect {
                if (it.accessToken.isNullOrBlank()) {
                    _tokenResult.value = ResponseResult.Error(Exception())
                    _token.value = ""
                    _refreshToken.value = ""
                } else {
                    _tokenResult.value = ResponseResult.Success(it.accessToken)
                    _token.value = it.accessToken
                    _refreshToken.value = it.refreshToken
                }
            }
        }
    }
}