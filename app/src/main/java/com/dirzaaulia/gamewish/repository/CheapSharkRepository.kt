package com.dirzaaulia.gamewish.repository

import androidx.annotation.WorkerThread
import com.dirzaaulia.gamewish.base.ResponseResult
import com.dirzaaulia.gamewish.base.executeWithData
import com.dirzaaulia.gamewish.base.executeWithResponse
import com.dirzaaulia.gamewish.data.model.cheapshark.Deals
import com.dirzaaulia.gamewish.data.request.cheapshark.DealsRequest
import com.dirzaaulia.gamewish.network.cheapshark.CheapSharkService
import com.dirzaaulia.gamewish.utils.CheapSharkConstant
import com.dirzaaulia.gamewish.utils.replaceIfNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CheapSharkRepository @Inject constructor(
    private val service: CheapSharkService
) {
    suspend fun getDeals(
        request: DealsRequest,
        page: Int
    ): ResponseResult<List<Deals>> {
        return withContext(Dispatchers.IO) {
            executeWithData {
                service.getGameDeals(
                    storeID = request.storeID,
                    pageNumber = page,
                    pageSize = CheapSharkConstant.CHEAPSHARK_PAGE_SIZE_TEN,
                    lowerPrice = request.lowerPrice,
                    upperPrice = request.upperPrice,
                    title = request.title,
                    aaa = request.aaa
                ).body().replaceIfNull()
            }
        }
    }

    @WorkerThread
    fun getStoreList() = flow {
        emit(ResponseResult.Loading)
        emit(executeWithResponse { service.getStoresList() })
    }
}