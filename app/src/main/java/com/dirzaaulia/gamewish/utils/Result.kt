package com.dirzaaulia.gamewish.utils

import androidx.paging.PagingSource
import com.dirzaaulia.gamewish.base.NotFoundException
import com.dirzaaulia.gamewish.base.ResponseResult

val ResponseResult<*>?.isInitialState get() = this != null && this is ResponseResult.Success && data == null

val ResponseResult<*>?.isSucceeded get() = this != null && this is ResponseResult.Success && data != null

val ResponseResult<*>?.isError get() = this != null && this is ResponseResult.Error

val ResponseResult<*>?.isLoading get() = this != null && this is ResponseResult.Loading

inline infix fun <T, Value : Any> ResponseResult<T>?.runSucceeded(predicate: (data: T) -> Value): Value? {
    if (this != null && this.isSucceeded && this is ResponseResult.Success && this.data != null) {
        return predicate.invoke(this.data)
    }
    return null
}

inline infix fun <T> ResponseResult<T>.success(predicate: (data: T) -> Unit): ResponseResult<T> {
    if (this is ResponseResult.Success && this.data != null) {
        predicate.invoke(this.data)
    }
    return this
}

inline infix fun <T> ResponseResult<T>.error(predicate: (data: Throwable) -> Unit) {
    if (this is ResponseResult.Error) {
        predicate.invoke(this.throwable)
    }
}

inline infix fun <T, Value : Any> ResponseResult<T>.pagingSucceeded(
    predicate: (data: T) -> PagingSource.LoadResult<Int, Value>
): PagingSource.LoadResult<Int, Value> {
    return if (this is ResponseResult.Success && this.data != null) {
        predicate.invoke(this.data)
    } else {
        if (this is ResponseResult.Error) {
            PagingSource.LoadResult.Error(this.throwable)
        } else {
            PagingSource.LoadResult.Error(NotFoundException())
        }
    }
}