package com.dirzaaulia.gamewish.repository

import androidx.annotation.WorkerThread
import com.dirzaaulia.gamewish.base.NotFoundException
import com.dirzaaulia.gamewish.base.ResponseResult
import com.dirzaaulia.gamewish.network.myanimelist.MyAnimeListBaseUrlService
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MyAnimeListRepository @Inject constructor(
    private val baseUrlService: MyAnimeListBaseUrlService
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
}