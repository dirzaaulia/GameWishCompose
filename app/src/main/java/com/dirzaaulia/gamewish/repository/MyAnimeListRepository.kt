package com.dirzaaulia.gamewish.repository

import androidx.annotation.WorkerThread
import com.dirzaaulia.gamewish.base.NotFoundException
import com.dirzaaulia.gamewish.base.ResponseResult
import com.dirzaaulia.gamewish.base.executeWithResponse
import com.dirzaaulia.gamewish.data.model.myanimelist.ParentNode
import com.dirzaaulia.gamewish.extension.error
import com.dirzaaulia.gamewish.extension.isError
import com.dirzaaulia.gamewish.network.myanimelist.MyAnimeListApiUrlService
import com.dirzaaulia.gamewish.network.myanimelist.MyAnimeListBaseUrlService
import com.dirzaaulia.gamewish.utils.MyAnimeListConstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import timber.log.Timber
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
        try {
            baseUrlService.getMyAnimeListToken(
                clientId, code, codeVerifier, grantType
            ).body()?.let {
                emit(ResponseResult.Success(it))
            } ?: run {
                throw  NotFoundException()
            }
        } catch (e: Exception) {
            emit(ResponseResult.Error(e))
        }
    }

    @WorkerThread
    fun getMyAnimeListRefreshToken(refreshToken: String) = flow {
        try {
            baseUrlService.getMyAnimeListRefreshToken(
                MyAnimeListConstant.MYANIMELIST_CLIENT_ID,
                "refresh_token",
                refreshToken
            ).body()?.let {
                emit(ResponseResult.Success(it))
            } ?: run {
                throw NotFoundException()
            }
        } catch (e: Exception) {
            emit(ResponseResult.Error(e))
        }
    }

    @WorkerThread
    fun getMyAnimeListUser(accessToken: String) = flow {
        try {
            val bearerAccessToken = String.format("Bearer $accessToken")
            apiUrlService.getMyAnimeListUser(bearerAccessToken).body()?.let {
                emit(ResponseResult.Success(it))
            } ?: run {
                throw NotFoundException()
            }
        } catch (e: Exception) {
            emit(ResponseResult.Error(e))
        }
    }

    @WorkerThread
    fun getMyAnimeListAnimeDetails(
        accessToken: String,
        animeId: Long
    ) = flow {
        try {
            val bearerAccessToken = String.format("Bearer $accessToken")
            apiUrlService.getMyAnimeListAnimeDetails(
                bearerAccessToken,
                animeId = animeId
            ).body()?.let {
                emit(ResponseResult.Success(it))
            } ?: run {
                throw NotFoundException()
            }
        } catch (e: Exception) {
            emit(ResponseResult.Error(e))
        }
    }

    suspend fun getUserAnimeList(
        accessToken: String,
        listStatus: String,
        offset: Int
    ): ResponseResult<List<ParentNode>> {
        val bearerAccessToken = String.format("Bearer $accessToken")
        return withContext(Dispatchers.IO) {
            executeWithResponse {
                apiUrlService.getMyAnimeListAnimeList(
                    bearerAccessToken,
                    status = listStatus,
                    offset = offset
                ).body()?.data ?: emptyList()
            }
        }
    }

    suspend fun getSeasonalAnime(
        accessToken: String,
        offset: Int,
        seasonalQuery: String
    ) : ResponseResult<List<ParentNode>> {
        val bearerAccessToken = String.format("Bearer $accessToken")
        val seasonal = seasonalQuery.split(" ")
        val season = seasonal[0]
        val year = seasonal[1]
        return withContext(Dispatchers.IO) {
            executeWithResponse {
                apiUrlService.getMyAnimeListSeasonalAnime(
                    bearerAccessToken,
                    season = season,
                    year = year,
                    offset = offset
                ).body()?.data ?: emptyList()
            }
        }
    }
}