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
import com.dirzaaulia.gamewish.data.model.myanimelist.User
import com.dirzaaulia.gamewish.data.model.rawg.Stores
import com.dirzaaulia.gamewish.data.model.wishlist.GameWishlist
import com.dirzaaulia.gamewish.data.model.wishlist.MovieWishlist
import com.dirzaaulia.gamewish.data.request.cheapshark.DealsRequest
import com.dirzaaulia.gamewish.data.response.myanimelist.MyAnimeListTokenResponse
import com.dirzaaulia.gamewish.extension.error
import com.dirzaaulia.gamewish.extension.success
import com.dirzaaulia.gamewish.network.cheapshark.paging.CheapSharkPagingSource
import com.dirzaaulia.gamewish.network.myanimelist.paging.MyAnimeListPagingSource
import com.dirzaaulia.gamewish.repository.*
import com.dirzaaulia.gamewish.utils.FirebaseState
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
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

    private val _refreshToken: MutableStateFlow<String> = MutableStateFlow("")

    private val _myAnimeListUser: MutableStateFlow<User> = MutableStateFlow(
        User(null, null, null, null, null, null, null)
    )
    val myAnimeListUser = _myAnimeListUser.asStateFlow()

    private val _googleProfileImage: MutableStateFlow<String> = MutableStateFlow("")
    val googleProfileImage = _googleProfileImage.asStateFlow()

    private val _googleUsername: MutableStateFlow<String> = MutableStateFlow("")
    val googleUsername = _googleUsername.asStateFlow()

    private val _myAnimeListTokenResult: MutableStateFlow<ResponseResult<MyAnimeListTokenResponse>?>
        = MutableStateFlow(null)
    val myAnimeListTokenResult = _myAnimeListTokenResult.asStateFlow()

    private val _errorMessage: MutableStateFlow<String> = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    val gameQuery = MutableStateFlow("")
    val gameStatus = MutableStateFlow("")
    val listWishlist = gameQuery
        .flatMapLatest { query ->
            gameStatus.flatMapLatest {
                databaseRepository.getGameFilteredWishlist(query, it)
            }
        }.cachedIn(viewModelScope)

    val dealsRequest = MutableStateFlow(
        DealsRequest("1", 0, 1000, "", false)
    )
    val deals: Flow<PagingData<Deals>> = dealsRequest
        .flatMapLatest {
            delay(5000)
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
                        it,
                        "",
                        ""
                    )
                }.flow.cachedIn(viewModelScope)
            }
        }

    val mangaStatus = MutableStateFlow("")
    val mangaList: Flow<PagingData<ParentNode>> = _token
        .flatMapLatest { accessToken ->
            mangaStatus.flatMapLatest {
                Pager(PagingConfig(pageSize = 10)) {
                    MyAnimeListPagingSource(
                        myAnimeListRepository,
                        5,
                        accessToken,
                        it,
                        "",
                        ""
                    )
                }.flow.cachedIn(viewModelScope)
            }
        }

    val movieQuery = MutableStateFlow("")
    val movieStatus = MutableStateFlow("")
    val movieWishlist = movieQuery
        .flatMapLatest { query ->
            movieStatus.flatMapLatest {
                databaseRepository.getMovieFilteredWishlist(query, it)
            }
        }.cachedIn(viewModelScope)

    val tvShowQuery = MutableStateFlow("")
    val tvShowStatus = MutableStateFlow("")
    val tvShowWishlist = tvShowQuery
        .flatMapLatest { query ->
            tvShowStatus.flatMapLatest {
                databaseRepository.getTVShowFilteredWishlist(query, it)
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
                        if ((exception.message?.contains("Data Not Found", true)
                                    == true) && _refreshToken.value.isNotBlank()) {
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
        setUserAuthId("")
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
            .onEach {
                it.success { data ->
                    _stores.value = data
                }
                _storesResult.value = it
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
                    setAnimeStatus("")
                }
                it.error { e ->
                    val message = e.message.toString()
                    if (message.contains("HTTP 401", true)) {
                        getMyAnimeListRefreshToken()
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun getMyAnimeListRefreshToken() {
        myAnimeListRepository.getMyAnimeListRefreshToken(_refreshToken.value)
            .onEach {
                it.success { data ->
                    Timber.i("Access Token : ${data.accessToken}")
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
                    setAnimeStatus("")
                    _myAnimeListTokenResult.value = it
                }
                it.error { exception ->
                    Timber.e(exception)
                    _errorMessage.value = "Something went wrong when getting data from MyAnimeList. Please try it again later!"
                    _myAnimeListTokenResult.value = it
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

    fun syncGameWishlist(uid: String) {
        viewModelScope.launch {
            Timber.i(uid)
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