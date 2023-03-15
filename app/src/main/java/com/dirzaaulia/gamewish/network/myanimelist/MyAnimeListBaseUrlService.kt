package com.dirzaaulia.gamewish.network.myanimelist

import com.dirzaaulia.gamewish.data.response.myanimelist.MyAnimeListTokenResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface MyAnimeListBaseUrlService {
    @FormUrlEncoded
    @POST("v1/oauth2/token")
    suspend fun getMyAnimeListToken(
        @Field("client_id") clientId: String,
        @Field("code") code: String,
        @Field("code_verifier") codeVerifier: String,
        @Field("grant_type") grantType: String
    ): Response<MyAnimeListTokenResponse>

    @FormUrlEncoded
    @POST("v1/oauth2/token")
    suspend fun getMyAnimeListRefreshToken(
        @Field("client_id") clientId: String,
        @Field("grant_type") grantType: String,
        @Field("refresh_token") refreshToken: String
    ): Response<MyAnimeListTokenResponse>
}