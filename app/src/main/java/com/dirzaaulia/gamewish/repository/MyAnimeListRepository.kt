package com.dirzaaulia.gamewish.repository

import androidx.annotation.WorkerThread
import com.dirzaaulia.gamewish.base.ResponseResult
import com.dirzaaulia.gamewish.base.executeWithData
import com.dirzaaulia.gamewish.base.executeWithResponse
import com.dirzaaulia.gamewish.data.model.myanimelist.ParentNode
import com.dirzaaulia.gamewish.network.myanimelist.MyAnimeListApiUrlService
import com.dirzaaulia.gamewish.network.myanimelist.MyAnimeListBaseUrlService
import com.dirzaaulia.gamewish.utils.MyAnimeListConstant
import com.dirzaaulia.gamewish.utils.OtherConstant
import com.dirzaaulia.gamewish.utils.replaceIfNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MyAnimeListRepository @Inject constructor(
    private val baseUrlService: MyAnimeListBaseUrlService,
    private val apiUrlService: MyAnimeListApiUrlService
) {

    @WorkerThread
    fun getMyAnimeListToken(
        clientId: String,
        code: String,
        codeVerifier: String,
        grantType: String
    ) = flow {
        emit(ResponseResult.Loading)
        emit(
            executeWithResponse {
                baseUrlService.getMyAnimeListToken(
                    clientId = clientId,
                    code = code,
                    codeVerifier = codeVerifier,
                    grantType = grantType
                )
            }
        )
    }

    @WorkerThread
    fun getMyAnimeListRefreshToken(refreshToken: String) = flow {
        emit(ResponseResult.Loading)
        emit(
            executeWithResponse {
                baseUrlService.getMyAnimeListRefreshToken(
                    MyAnimeListConstant.MYANIMELIST_CLIENT_ID,
                    MyAnimeListConstant.MYANIMELIST_GRANT_TYPE_REFRESH_TOKEN,
                    refreshToken
                )
            }
        )
    }

    @WorkerThread
    fun getMyAnimeListUser(accessToken: String) = flow {
        val bearerAccessToken =
            String.format(MyAnimeListConstant.MYANIMELIST_BEARER_FORMAT, accessToken)
        emit(ResponseResult.Loading)
        emit(
            executeWithResponse {
                apiUrlService.getMyAnimeListUser(bearerAccessToken)
            }
        )
    }

    @WorkerThread
    fun getMyAnimeListAnimeDetails(
        accessToken: String,
        animeId: Long
    ) = flow {
        val bearerAccessToken =
            String.format(MyAnimeListConstant.MYANIMELIST_BEARER_FORMAT, accessToken)
        emit(ResponseResult.Loading)
        emit(
            executeWithResponse {
                apiUrlService.getMyAnimeListAnimeDetails(
                    authorization = bearerAccessToken,
                    animeId = animeId
                )
            }
        )
    }

    @WorkerThread
    fun getMyAnimeListMangaDetails(
        accessToken: String,
        mangaId: Long
    ) = flow {
        val bearerAccessToken =
            String.format(MyAnimeListConstant.MYANIMELIST_BEARER_FORMAT, accessToken)
        emit(ResponseResult.Loading)
        emit(
            executeWithResponse {
                apiUrlService.getMyAnimeListMangaDetails(
                    authorization = bearerAccessToken,
                    mangaId = mangaId
                )
            }
        )
    }

    @WorkerThread
    fun updateMyAnimeListAnimeList(
        accessToken: String,
        animeId: Long,
        status: String,
        isRewatching: Boolean,
        score: Int,
        numberWatched: Int
    ) = flow {
        val bearerAccessToken =
            String.format(MyAnimeListConstant.MYANIMELIST_BEARER_FORMAT, accessToken)
        emit(ResponseResult.Loading)
        emit(
            executeWithResponse {
                apiUrlService.updateMyAnimeListAnimeList(
                    authorization = bearerAccessToken,
                    id = animeId,
                    status = status,
                    isRewatching = isRewatching,
                    score = score,
                    episode = numberWatched
                )
            }
        )
    }

    @WorkerThread
    fun deleteMyAnimeListAnimeList(
        accessToken: String,
        animeId: Long
    ) = flow {
        val bearerAccessToken =
            String.format(MyAnimeListConstant.MYANIMELIST_BEARER_FORMAT, accessToken)
        emit(ResponseResult.Loading)
        emit(
            executeWithResponse {
                apiUrlService.deleteMyAnimeListAnimeList(
                    authorization = bearerAccessToken,
                    id = animeId,
                )
            }
        )
    }

    @WorkerThread
    fun updateMyAnimeListMangaList(
        accessToken: String,
        mangaId: Long,
        status: String,
        isRereading: Boolean,
        score: Int,
        numberRead: Int
    ) = flow {
        val bearerAccessToken =
            String.format(MyAnimeListConstant.MYANIMELIST_BEARER_FORMAT, accessToken)
        emit(ResponseResult.Loading)
        emit(
            executeWithResponse {
                apiUrlService.updateMyAnimeListMangaList(
                    authorization = bearerAccessToken,
                    id = mangaId,
                    status = status,
                    isRereading = isRereading,
                    score = score,
                    episode = numberRead
                )
            }
        )
    }

    @WorkerThread
    fun deleteMyAnimeListMangaList(
        accessToken: String,
        mangaId: Long
    ) = flow {
        val bearerAccessToken = String.format(MyAnimeListConstant.MYANIMELIST_BEARER_FORMAT, accessToken)
        emit(ResponseResult.Loading)
        emit(
            executeWithResponse {
                apiUrlService.deleteMyAnimeListMangaList(
                    authorization = bearerAccessToken,
                    id = mangaId,
                )
            }
        )
    }

    suspend fun getUserAnimeList(
        accessToken: String,
        listStatus: String,
        offset: Int
    ): ResponseResult<List<ParentNode>> {
        val bearerAccessToken =
            String.format(MyAnimeListConstant.MYANIMELIST_BEARER_FORMAT, accessToken)
        return withContext(Dispatchers.IO) {
            executeWithData {
                apiUrlService.getMyAnimeListAnimeList(
                    bearerAccessToken,
                    status = listStatus,
                    offset = offset
                ).body()?.data.replaceIfNull()
            }
        }
    }

    suspend fun getUserMangaList(
        accessToken: String,
        listStatus: String,
        offset: Int
    ): ResponseResult<List<ParentNode>> {
        val bearerAccessToken = String.format(MyAnimeListConstant.MYANIMELIST_BEARER_FORMAT, accessToken)
        return withContext(Dispatchers.IO) {
            executeWithData {
                apiUrlService.getMyAnimeListMangaList(
                    bearerAccessToken,
                    status = listStatus,
                    offset = offset
                ).body()?.data.replaceIfNull()
            }
        }
    }

    suspend fun getSeasonalAnime(
        accessToken: String,
        offset: Int,
        seasonalQuery: String
    ): ResponseResult<List<ParentNode>> {
        val bearerAccessToken = String.format(MyAnimeListConstant.MYANIMELIST_BEARER_FORMAT, accessToken)
        val seasonal = seasonalQuery.split(OtherConstant.BLANK_SPACE)
        val season = seasonal[0]
        val year = seasonal[1]
        return withContext(Dispatchers.IO) {
            executeWithData {
                apiUrlService.getMyAnimeListSeasonalAnime(
                    bearerAccessToken,
                    season = season,
                    year = year,
                    offset = offset
                ).body()?.data.replaceIfNull()
            }
        }
    }

    suspend fun searchAnime(
        accessToken: String,
        offset: Int,
        searchQuery: String
    ): ResponseResult<List<ParentNode>> {
        val bearerAccessToken = String.format(MyAnimeListConstant.MYANIMELIST_BEARER_FORMAT, accessToken)
        return withContext(Dispatchers.IO) {
            executeWithData {
                apiUrlService.searchMyAnimeListAnime(
                    bearerAccessToken,
                    searchQuery,
                    offset
                ).body()?.data.replaceIfNull()
            }
        }
    }

    suspend fun searchManga(
        accessToken: String,
        offset: Int,
        searchQuery: String
    ): ResponseResult<List<ParentNode>> {
        val bearerAccessToken = String.format(MyAnimeListConstant.MYANIMELIST_BEARER_FORMAT, accessToken)
        return withContext(Dispatchers.IO) {
            executeWithData {
                apiUrlService.searchMyAnimeListManga(
                    bearerAccessToken,
                    searchQuery,
                    offset
                ).body()?.data.replaceIfNull()
            }
        }
    }
}