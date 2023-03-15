package com.dirzaaulia.gamewish.network.cheapshark

import com.dirzaaulia.gamewish.data.model.cheapshark.Deals
import com.dirzaaulia.gamewish.data.model.rawg.Stores
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CheapSharkService {

    @GET("deals")
    suspend fun getGameDeals(
        @Query("storeID") storeID: String?,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int,
        @Query("lowerPrice") lowerPrice: Long?,
        @Query("upperPrice") upperPrice: Long?,
        @Query("title") title: String?,
        @Query("AAA") aaa: Boolean?
    ): Response<List<Deals>>

    @GET("stores")
    suspend fun getStoresList(): Response<List<Stores>>
}