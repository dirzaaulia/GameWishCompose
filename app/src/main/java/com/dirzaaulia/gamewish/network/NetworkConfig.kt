package com.dirzaaulia.gamewish.network

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class NetworkConfig(
    client: OkHttpClient,
    moshi: Moshi,
    baseUrl: String
) {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    fun <T> create(clazz: Class<T>): T = retrofit.create(clazz)
}