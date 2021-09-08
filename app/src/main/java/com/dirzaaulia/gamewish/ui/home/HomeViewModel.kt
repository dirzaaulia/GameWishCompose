package com.dirzaaulia.gamewish.ui.home

import androidx.annotation.MainThread
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dirzaaulia.gamewish.base.ResponseResult
import com.dirzaaulia.gamewish.data.model.Wishlist
import com.dirzaaulia.gamewish.data.model.cheapshark.Deals
import com.dirzaaulia.gamewish.data.model.rawg.Stores
import com.dirzaaulia.gamewish.data.request.cheapshark.DealsRequest
import com.dirzaaulia.gamewish.data.response.myanimelist.MyAnimeListTokenResponse
import com.dirzaaulia.gamewish.extension.success
import com.dirzaaulia.gamewish.network.cheapshark.paging.CheapSharkPagingSource
import com.dirzaaulia.gamewish.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val protoRepository: ProtoRepository,
    private val databaseRepository: DatabaseRepository,
    private val cheapSharkRepository: CheapSharkRepository,
    private val firebaseRepository: FirebaseRepository,
    private val myAnimeListRepository: MyAnimeListRepository
) : ViewModel() {

    private val userPreferencesFlow = protoRepository.userPreferencesFlow

    val dealsRequest = MutableStateFlow(
        DealsRequest("1", 0, 1000, "", false)
    )

    private val _selectedBottomNav: MutableStateFlow<Int> = MutableStateFlow(0)
    val selectedBottomNav: StateFlow<Int> get() = _selectedBottomNav.asStateFlow()

    private val _selectedWishlistTab: MutableStateFlow<Int> = MutableStateFlow(0)
    val selectedWishlistTab: StateFlow<Int> get() = _selectedWishlistTab.asStateFlow()

    private var _storesResult : MutableStateFlow<ResponseResult<List<Stores>>?>
            = MutableStateFlow(null)
    val storesResult = _storesResult.asStateFlow()

    private val _stores : MutableStateFlow<List<Stores>?>
            = MutableStateFlow(null)
    val stores = _stores.asStateFlow()

    private var _userAuthId : MutableStateFlow<ResponseResult<String>?>
            = MutableStateFlow(null)
    var userAuthId = _userAuthId.asStateFlow()

    private var _tokenResult : MutableStateFlow<ResponseResult<String>?>
            = MutableStateFlow(null)
    val tokenResult = _tokenResult.asStateFlow()

    private var _token : MutableStateFlow<String?>
            = MutableStateFlow(null)
    val token = _token.asStateFlow()

    val query = MutableStateFlow("")
    val listWishlist = query
        .flatMapLatest {
            databaseRepository.getFilteredWishlist(it)
        }.cachedIn(viewModelScope)

    val deals: Flow<PagingData<Deals>> = dealsRequest
        .flatMapLatest {
            Pager(PagingConfig(pageSize = 10)) {
                CheapSharkPagingSource(
                    cheapSharkRepository,
                    it
                )
            }.flow.cachedIn(viewModelScope)
        }

    init {
        getUserAuthStatus()
        getAccessToken()
        getStores()
    }

//    fun getUserAuthStatus() {
//        firebaseRepository.getUserAuthId()
//            .onEach {
//                _userAuthId.value = it
//            }
//            .launchIn(viewModelScope)
//    }

    fun getUserAuthStatus() {
        viewModelScope.launch {
            userPreferencesFlow.collect {
                if (it.userAuthId.isNullOrBlank()) {
                    _userAuthId.value = ResponseResult.Error(Exception())
                } else {
                    _userAuthId.value = ResponseResult.Success(it.userAuthId)
                }
            }
        }
    }

    fun getAccessToken() {
        viewModelScope.launch {
            userPreferencesFlow.collect {
                if (it.accessToken.isNullOrBlank()) {
                    _tokenResult.value = ResponseResult.Error(Exception())
                } else {
                    _tokenResult.value = ResponseResult.Success(it.accessToken)
                    _token.value = it.accessToken
                }
            }
        }
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

    @MainThread
    fun setDealsRequest(request: DealsRequest) {
        dealsRequest.value = request
    }

    fun getStores() {
        cheapSharkRepository.getStoreList()
            .onEach {
                it.success { data ->
                    _stores.value = data
                }
                _storesResult.value = it
            }
            .launchIn(viewModelScope)
    }

    fun getMyAnimeListToken(
        clientId : String,
        code : String,
        codeVerifier : String,
        grantType : String
    ) {
        myAnimeListRepository.getMyAnimeListToken(clientId, code, codeVerifier, grantType)
            .onEach {
                it.success { data ->
                    data.accessToken?.let { accessToken ->
                        protoRepository.updateMyAnimeListAccessToken(accessToken)
                    }
                    data.refreshToken?.let { refreshToken ->
                        protoRepository.updateMyAnimeListRefreshToken(refreshToken)
                    }
                    data.expiresIn?.let { expiresIn ->
                        protoRepository.updateMyAnimeListExpresIn(expiresIn)
                    }
                }
            }
            .launchIn(viewModelScope)
    }
}