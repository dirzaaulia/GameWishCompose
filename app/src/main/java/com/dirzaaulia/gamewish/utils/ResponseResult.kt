package com.dirzaaulia.gamewish.utils

import com.google.android.gms.tasks.Task
import retrofit2.HttpException
import retrofit2.Response

/**
 * A generic class that holds a value or an exception
 */
sealed class ResponseResult<out R> {

    object Loading: ResponseResult<Nothing>()
    data class Success<out T>(val data: T) : ResponseResult<T>()
    data class Error(val throwable: Throwable) : ResponseResult<Nothing>()
}

inline fun <T> executeWithData(body: () -> T): ResponseResult<T> {
    return try {
        ResponseResult.Success(body.invoke())
    } catch (e: Exception) {
        e.printStackTrace()
        ResponseResult.Error(e)
    }
}

inline fun <T> executeWithResponse(body: () -> Response<T>): ResponseResult<T> {
    return try {
        val response = body.invoke()
        response.body()?.let {
            ResponseResult.Success(it)
        } ?: run {
            throw HttpException(response)
        }
    } catch (throwable: Throwable) {
        throwable.printStackTrace()
        ResponseResult.Error(throwable)
    }
}

inline fun executeFirebase(body: () -> Task<Void>): ResponseResult<Task<Void>> {
    return try {
        ResponseResult.Success(body())
    } catch (throwable: Throwable) {
        throwable.printStackTrace()
        ResponseResult.Error(throwable)
    }
}