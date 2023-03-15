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
import com.dirzaaulia.gamewish.data.model.cheapshark.Deals
import com.dirzaaulia.gamewish.data.model.myanimelist.ParentNode
import com.dirzaaulia.gamewish.data.model.myanimelist.ServiceCode
import com.dirzaaulia.gamewish.data.model.myanimelist.User
import com.dirzaaulia.gamewish.data.model.rawg.Stores
import com.dirzaaulia.gamewish.data.model.wishlist.GameWishlist
import com.dirzaaulia.gamewish.data.model.wishlist.MovieWishlist
import com.dirzaaulia.gamewish.data.request.cheapshark.DealsRequest
import com.dirzaaulia.gamewish.data.response.myanimelist.MyAnimeListTokenResponse
import com.dirzaaulia.gamewish.network.cheapshark.paging.CheapSharkPagingSource
import com.dirzaaulia.gamewish.network.myanimelist.paging.MyAnimeListPagingSource
import com.dirzaaulia.gamewish.repository.CheapSharkRepository
import com.dirzaaulia.gamewish.repository.DatabaseRepository
import com.dirzaaulia.gamewish.repository.FirebaseRepository
import com.dirzaaulia.gamewish.repository.MyAnimeListRepository
import com.dirzaaulia.gamewish.repository.ProtoRepository
import com.dirzaaulia.gamewish.utils.CheapSharkConstant
import com.dirzaaulia.gamewish.utils.FirebaseState
import com.dirzaaulia.gamewish.utils.MyAnimeListConstant
import com.dirzaaulia.gamewish.utils.OtherConstant
import com.dirzaaulia.gamewish.utils.checkContain
import com.dirzaaulia.gamewish.utils.error
import com.dirzaaulia.gamewish.utils.success
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
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
class HomeViewModel @Inject constructor(
    private val protoRepository: ProtoRepository,
    private val databaseRepository: DatabaseRepository,
    private val cheapSharkRepository: CheapSharkRepository,
    private val firebaseRepository: FirebaseRepository,
    private val myAnimeListRepository: MyAnimeListRepository
) : ViewModel() {

    val loadingState = MutableStateFlow(FirebaseState.IDLE)

    private val userPreferencesFlow = protoRepository.userPreferencesFlow

    private val _selectedBottomNav: MutableStateFlow<Int> = MutableStateFlow(OtherConstant.ZERO)
    val selectedBottomNav = _selectedBottomNav.asStateFlow()

    private val _selectedWishlistTab: MutableStateFlow<Int> = MutableStateFlow(OtherConstant.ZERO)
    val selectedWishlistTab = _selectedWishlistTab.asStateFlow()

    private var _storesResult: MutableStateFlow<ResponseResult<List<Stores>>?> =
        MutableStateFlow(null)
    val storesResult = _storesResult.asStateFlow()

    private val _stores: MutableStateFlow<List<Stores>?> = MutableStateFlow(null)
    val stores = _stores.asStateFlow()

    private val _userAuthId: MutableStateFlow<String?> = MutableStateFlow(null)
    val userAuthId = _userAuthId.asStateFlow()

    private var _tokenResult: MutableStateFlow<ResponseResult<String>?> = MutableStateFlow(null)
    val tokenResult = _tokenResult.asStateFlow()

    private var _token: MutableStateFlow<String> = MutableStateFlow(OtherConstant.EMPTY_STRING)
    val token = _token.asStateFlow()

    private val _refreshToken: MutableStateFlow<String> = MutableStateFlow(OtherConstant.EMPTY_STRING)

    private val _myAnimeListUser: MutableStateFlow<User> = MutableStateFlow(User.default())
    val myAnimeListUser = _myAnimeListUser.asStateFlow()

    private val _googleProfileImage: MutableStateFlow<String> = MutableStateFlow(OtherConstant.EMPTY_STRING)
    val googleProfileImage = _googleProfileImage.asStateFlow()

    private val _googleUsername: MutableStateFlow<String> = MutableStateFlow(OtherConstant.EMPTY_STRING)
    val googleUsername = _googleUsername.asStateFlow()

    private val _myAnimeListTokenResult: MutableStateFlow<ResponseResult<MyAnimeListTokenResponse>?>
        = MutableStateFlow(null)
    val myAnimeListTokenResult = _myAnimeListTokenResult.asStateFlow()

    private val _errorMessage: MutableStateFlow<String> = MutableStateFlow(OtherConstant.EMPTY_STRING)
    val errorMessage = _errorMessage.asStateFlow()

    val gameQuery = MutableStateFlow(OtherConstant.EMPTY_STRING)
    val gameStatus = MutableStateFlow(OtherConstant.ALL)
    val listWishlist = gameQuery
        .flatMapLatest { query ->
            gameStatus.flatMapLatest { status ->
                databaseRepository.getGameFilteredWishlist(query, status)
            }
        }.cachedIn(viewModelScope)

    val dealsRequest = MutableStateFlow(DealsRequest.default())
    val deals: Flow<PagingData<Deals>> = dealsRequest
        .flatMapLatest { request ->
            Pager(PagingConfig(pageSize = CheapSharkConstant.CHEAPSHARK_PAGE_SIZE_TEN)) {
                CheapSharkPagingSource(
                    cheapSharkRepository,
                    request
                )
            }.flow.cachedIn(viewModelScope)
        }

    val animeStatus = MutableStateFlow(OtherConstant.ALL)
    val animeList: Flow<PagingData<ParentNode>> = _token
        .flatMapLatest { accessToken ->
            animeStatus.flatMapLatest { status ->
                Pager(PagingConfig(pageSize = MyAnimeListConstant.MYANIMELIST_PAGE_SIZE_TEN)) {
                    MyAnimeListPagingSource(
                        repository = myAnimeListRepository,
                        serviceCode = ServiceCode.USER_ANIME_LIST,
                        accessToken = accessToken,
                        listStatus = status,
                        seasonalQuery = OtherConstant.EMPTY_STRING,
                        searchQuery = OtherConstant.EMPTY_STRING
                    )
                }.flow.cachedIn(viewModelScope)
            }
        }

    val mangaStatus = MutableStateFlow(OtherConstant.ALL)
    val mangaList: Flow<PagingData<ParentNode>> = _token
        .flatMapLatest { accessToken ->
            mangaStatus.flatMapLatest { status ->
                Pager(PagingConfig(pageSize = MyAnimeListConstant.MYANIMELIST_PAGE_SIZE_TEN)) {
                    MyAnimeListPagingSource(
                        repository = myAnimeListRepository,
                        serviceCode = ServiceCode.USER_MANGA_LIST,
                        accessToken = accessToken,
                        listStatus = status,
                        seasonalQuery = OtherConstant.EMPTY_STRING,
                        searchQuery = OtherConstant.EMPTY_STRING
                    )
                }.flow.cachedIn(viewModelScope)
            }
        }

    val movieQuery = MutableStateFlow(OtherConstant.EMPTY_STRING)
    val movieStatus = MutableStateFlow(OtherConstant.ALL)
    val movieWishlist = movieQuery
        .flatMapLatest { query ->
            movieStatus.flatMapLatest { status ->
                databaseRepository.getMovieFilteredWishlist(query, status)
            }
        }.cachedIn(viewModelScope)

    val tvShowQuery = MutableStateFlow(OtherConstant.EMPTY_STRING)
    val tvShowStatus = MutableStateFlow(OtherConstant.ALL)
    val tvShowWishlist = tvShowQuery
        .flatMapLatest { query ->
            tvShowStatus.flatMapLatest { status ->
                databaseRepository.getTVShowFilteredWishlist(query, status)
            }
        }.cachedIn(viewModelScope)

    val myAnimeListUserResult: Flow<ResponseResult<User>> = _token
        .flatMapLatest { token ->
            myAnimeListRepository.getMyAnimeListUser(token)
                .onEach {
                    it.success { data ->
                        _myAnimeListUser.value = data
                    }
                    it.error { exception ->
                        if (exception.message.checkContain(MyAnimeListConstant.MYANIMELIST_DATA_NOT_FOUND)
                            && _refreshToken.value.isNotBlank()) {
                            getMyAnimeListRefreshToken()
                        }
                    }
                }
        }

    init {
        getUserAuthStatus()
        getGoogleUserData()
        getAccessToken()
        getStores()
    }

    @MainThread
    fun setGameQuery(value: String) {
        gameQuery.value = value
    }

    @MainThread
    fun setGameStatus(status: String) {
        gameStatus.value = status
    }

    @MainThread
    fun setMovieQuery(value: String) {
        movieQuery.value = value
    }

    @MainThread
    fun setMovieStatus(status: String) {
        movieStatus.value = status
    }

    @MainThread
    fun setTVShowQuery(value: String) {
        tvShowQuery.value = value
    }

    @MainThread
    fun setTVShowStatus(status: String) {
        tvShowStatus.value = status
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

    @MainThread
    fun setMangaStatus(request: String) {
        mangaStatus.value = request
    }

    private fun addToGameWishlist(gameWishlist: GameWishlist) {
        viewModelScope.launch {
            databaseRepository.addToGameWishlist(gameWishlist)
        }
    }

    private fun addToMovieWishlist(movieWishlist: MovieWishlist) {
        viewModelScope.launch {
            databaseRepository.addToMovieWishlist(movieWishlist)
        }
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

    fun logoutGoogle() {
        val auth = getFirebaseAuth()
        auth.signOut()
        setUserAuthId(OtherConstant.EMPTY_STRING)
    }

    fun setUserAuthId(uid: String) {
        viewModelScope.launch {
            protoRepository.updateUserAuthId(uid)
        }
    }

    fun setAccessToken(accessToken: String) {
        viewModelScope.launch {
            protoRepository.updateMyAnimeListAccessToken(accessToken)
        }
    }

    fun getStores() {
        cheapSharkRepository.getStoreList()
            .onEach { result ->
                result.success { data ->
                    _stores.value = data
                }
                _storesResult.value = result
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
                    getAccessToken()
                    setAnimeStatus(OtherConstant.EMPTY_STRING)
                }
                it.error { error ->
                    if (error.message.checkContain(OtherConstant.HTTP_ERROR_401))
                        getMyAnimeListRefreshToken()
                }
            }
            .launchIn(viewModelScope)
    }

    fun getMyAnimeListRefreshToken() {
        myAnimeListRepository.getMyAnimeListRefreshToken(_refreshToken.value)
            .onEach { result ->
                result.success { data ->
                    data.accessToken?.let { accessToken ->
                        protoRepository.updateMyAnimeListAccessToken(accessToken)
                    }
                    data.refreshToken?.let { refreshToken ->
                        protoRepository.updateMyAnimeListRefreshToken(refreshToken)
                    }
                    data.expiresIn?.let { expiresIn ->
                        protoRepository.updateMyAnimeListExpresIn(expiresIn)
                    }
                    getAccessToken()
                    setAnimeStatus(OtherConstant.EMPTY_STRING)
                    _myAnimeListTokenResult.value = result
                }
                result.error {
                    _errorMessage.value = MyAnimeListConstant.MYANIMELIST_GENERAL_ERROR
                    _myAnimeListTokenResult.value = result
                }
            }
            .launchIn(viewModelScope)
    }

    fun getUserAuthStatus() {
        viewModelScope.launch {
            userPreferencesFlow.collect {
                if (it.userAuthId.isNullOrBlank())
                    _userAuthId.value = OtherConstant.EMPTY_STRING
                else
                    _userAuthId.value = it.userAuthId
            }
        }
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

    fun syncGameWishlist(uid: String) {
        viewModelScope.launch {
            val data = firebaseRepository.getAllGameWishlist(uid)?.toObjects(GameWishlist::class.java)
            data?.forEach {
                addToGameWishlist(it)
            }
        }
    }

    fun syncMovieWishlist(uid: String) {
        viewModelScope.launch {
            val data = firebaseRepository.getAllMovieWishlist(uid)?.toObjects(MovieWishlist::class.java)
            data?.forEach {
                addToMovieWishlist(it)
            }
        }
    }
}