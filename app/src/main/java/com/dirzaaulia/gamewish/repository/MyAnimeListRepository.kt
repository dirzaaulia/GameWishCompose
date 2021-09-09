package com.dirzaaulia.gamewish.repository

import androidx.annotation.WorkerThread
import com.dirzaaulia.gamewish.base.NotFoundException
import com.dirzaaulia.gamewish.base.ResponseResult
import com.dirzaaulia.gamewish.base.executeWithResponse
import com.dirzaaulia.gamewish.data.model.myanimelist.ParentNode
import com.dirzaaulia.gamewish.network.myanimelist.MyAnimeListApiUrlService
import com.dirzaaulia.gamewish.network.myanimelist.MyAnimeListBaseUrlService
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
}