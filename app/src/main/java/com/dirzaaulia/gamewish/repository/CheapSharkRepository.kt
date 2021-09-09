package com.dirzaaulia.gamewish.repository

import androidx.annotation.WorkerThread
import com.dirzaaulia.gamewish.base.NotFoundException
import com.dirzaaulia.gamewish.base.ResponseResult
import com.dirzaaulia.gamewish.base.executeWithResponse
import com.dirzaaulia.gamewish.data.model.cheapshark.Deals
import com.dirzaaulia.gamewish.data.request.cheapshark.DealsRequest
import com.dirzaaulia.gamewish.network.cheapshark.CheapSharkService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CheapSharkRepository @Inject constructor(
    private val service: CheapSharkService
) {
    suspend fun getDeals(request: DealsRequest, page: Int): ResponseResult<List<Deals>> {
        return withContext(Dispatchers.IO) {
            executeWithResponse {
                service.getGameDeals(
                    request.storeID,
                    page,
                    10,
                    request.lowerPrice,
                    request.upperPrice,
                    request.title,
                    request.AAA
                ).body() ?: emptyList()
            }
        }
    }

    @WorkerThread
    fun getStoreList() = flow {
        try {
            service.getStoresList().body()?.let {
                emit(ResponseResult.Success(it))
            } ?: run {
                throw  NotFoundException()
            }
        } catch (e: Exception) {
            emit(ResponseResult.Error(e))
        }
    }
}