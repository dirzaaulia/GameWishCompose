package com.dirzaaulia.gamewish.ui.search

import androidx.annotation.MainThread
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.dirzaaulia.gamewish.data.request.myanimelist.SearchGameRequest
import com.dirzaaulia.gamewish.network.cheapshark.paging.CheapSharkPagingSource
import com.dirzaaulia.gamewish.network.rawg.paging.RawgGenrePagingSource
import com.dirzaaulia.gamewish.network.rawg.paging.RawgPlatformPagingSource
import com.dirzaaulia.gamewish.network.rawg.paging.RawgPublisherPagingSource
import com.dirzaaulia.gamewish.network.rawg.paging.RawgSearchPagingSource
import com.dirzaaulia.gamewish.repository.RawgRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val rawgRepository: RawgRepository
) : ViewModel() {

    private val _selectedBottomNav: MutableStateFlow<Int> = MutableStateFlow(0)
    val selectedBottomNav: StateFlow<Int> get() = _selectedBottomNav.asStateFlow()

    private val _selectedSearchGameTab: MutableStateFlow<Int> = MutableStateFlow(0)
    val selectedSearchGameTab: StateFlow<Int> get() = _selectedSearchGameTab.asStateFlow()

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
    val searchGameList = searchGameRequest.
    flatMapLatest {
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


    @MainThread
    fun selectBottomNavMenu(@StringRes tab: Int) {
        _selectedBottomNav.value = tab
    }

    @MainThread
    fun selectSearchGameTab(tabIndex: Int) {
        _selectedSearchGameTab.value = tabIndex
    }

    @MainThread
    fun setSearchGameRequest(request: SearchGameRequest) {
        searchGameRequest.value = request
    }
}