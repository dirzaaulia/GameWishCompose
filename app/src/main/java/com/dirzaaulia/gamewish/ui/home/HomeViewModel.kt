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
import com.dirzaaulia.gamewish.data.model.myanimelist.ParentNode
import com.dirzaaulia.gamewish.data.model.myanimelist.User
import com.dirzaaulia.gamewish.data.request.cheapshark.DealsRequest
import com.dirzaaulia.gamewish.extension.success
import com.dirzaaulia.gamewish.network.cheapshark.paging.CheapSharkPagingSource
import com.dirzaaulia.gamewish.network.myanimelist.paging.MyAnimeListPagingSource
import com.dirzaaulia.gamewish.repository.*
import com.dirzaaulia.gamewish.utils.FirebaseState
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
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

    val loadingState = MutableStateFlow(FirebaseState.IDLE)

    private val userPreferencesFlow = protoRepository.userPreferencesFlow

    private val _selectedBottomNav: MutableStateFlow<Int> = MutableStateFlow(0)
    val selectedBottomNav: StateFlow<Int> get() = _selectedBottomNav.asStateFlow()

    private val _selectedWishlistTab: MutableStateFlow<Int> = MutableStateFlow(0)
    val selectedWishlistTab: StateFlow<Int> get() = _selectedWishlistTab.asStateFlow()

    private var _storesResult: MutableStateFlow<ResponseResult<List<Stores>>?> =
        MutableStateFlow(null)
    val storesResult = _storesResult.asStateFlow()

    private val _stores: MutableStateFlow<List<Stores>?> = MutableStateFlow(null)
    val stores = _stores.asStateFlow()

    private val _userAuthId: MutableStateFlow<String?> = MutableStateFlow(null)
    val userAuthId = _userAuthId.asStateFlow()

    private var _tokenResult: MutableStateFlow<ResponseResult<String>?> = MutableStateFlow(null)
    val tokenResult = _tokenResult.asStateFlow()

    private var _token: MutableStateFlow<String> = MutableStateFlow("")
    val token = _token.asStateFlow()

    private val _myAnimeListUser: MutableStateFlow<User> = MutableStateFlow(
        User(null, null, null, null, null, null, null)
    )
    val myAnimeListUser = _myAnimeListUser.asStateFlow()

    private val _googleProfileImage: MutableStateFlow<String> = MutableStateFlow("")
    val googleProfileImage = _googleProfileImage.asStateFlow()

    private val _googleUsername: MutableStateFlow<String> = MutableStateFlow("")
    val googleUsername = _googleUsername.asStateFlow()

    val query = MutableStateFlow("")
    val listWishlist = query
        .flatMapLatest {
            databaseRepository.getFilteredWishlist(it)
        }.cachedIn(viewModelScope)

    val dealsRequest = MutableStateFlow(
        DealsRequest("1", 0, 1000, "", false)
    )
    val deals: Flow<PagingData<Deals>> = dealsRequest
        .flatMapLatest {
            Pager(PagingConfig(pageSize = 10)) {
                CheapSharkPagingSource(
                    cheapSharkRepository,
                    it
                )
            }.flow.cachedIn(viewModelScope)
        }

    val animeStatus = MutableStateFlow("")
    val animeList: Flow<PagingData<ParentNode>> = _token
        .flatMapLatest { accessToken ->
            animeStatus.flatMapLatest {
                Pager(PagingConfig(pageSize = 10)) {
                    MyAnimeListPagingSource(
                        myAnimeListRepository,
                        1,
                        accessToken,
                        it
                    )
                }.flow.cachedIn(viewModelScope)
            }
        }


    init {
        getUserAuthStatus()
        getGoogleUserData()
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

    @MainThread
    fun setDealsRequest(request: DealsRequest) {
        dealsRequest.value = request
    }

    @MainThread
    fun setAnimeStatus(request: String) {
        animeStatus.value = request
    }

    fun getFirebaseAuth(): FirebaseAuth {
        return firebaseRepository.getFirebaseAuth()
    }

    fun getGoogleUserData() {
        val auth = firebaseRepository.getFirebaseAuth()
        _googleProfileImage.value = auth.currentUser?.photoUrl.toString()
        _googleUsername.value = auth.currentUser?.displayName.toString()
    }

    fun signWithCredential(credential: AuthCredential) = viewModelScope.launch {
        try {
            loadingState.emit(FirebaseState.LOADING)
            firebaseRepository.signIn(credential)
            loadingState.emit(FirebaseState.LOADED)
        } catch (e: Exception) {
            loadingState.emit(FirebaseState.error(e.localizedMessage))
        }
    }

    fun setUserAuthId(uid: String) {
        viewModelScope.launch {
            protoRepository.updateUserAuthId(uid)
        }
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

    fun addToWishlist(wishlist: Wishlist) {
        viewModelScope.launch {
            databaseRepository.addToWishlist(wishlist)
        }
    }

    fun getMyAnimeListUser() {
        myAnimeListRepository.getMyAnimeListUser(_token.value)
            .onEach {
                it.success {
                    _myAnimeListUser.value = it
                }
            }
            .launchIn(viewModelScope)
    }

    fun getMyAnimeListToken(
        clientId: String,
        code: String,
        codeVerifier: String,
        grantType: String
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

    fun getUserAuthStatus() {
        viewModelScope.launch {
            userPreferencesFlow.collect {
                if (it.userAuthId.isNullOrBlank()) {
                    _userAuthId.value = ""
                } else {
                    _userAuthId.value = it.userAuthId
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

    fun getUserMyAnimeList(listStatus: String): Flow<PagingData<ParentNode>> {
        return if (!_token.value.isNullOrBlank()) {
            Pager(PagingConfig(pageSize = 10)) {
                MyAnimeListPagingSource(
                    myAnimeListRepository,
                    1,
                    _token.value!!,
                    listStatus
                )
            }.flow.cachedIn(viewModelScope)
        } else {
            emptyFlow()
        }
    }

}