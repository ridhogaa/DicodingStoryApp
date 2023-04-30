package com.ergea.dicodingstoryapp.wrapper

import com.ergea.dicodingstoryapp.data.remote.model.error.ResponseError
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.HttpException

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

sealed class Resource<T>(
    val payload: T? = null,
    val message: String? = null,
    val exception: Exception? = null,
) {
    class Success<T>(val data: T?) : Resource<T>(data)
    class Empty<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(exception: Exception?, message: String?) :
        Resource<T>(message = message, exception = exception)

    class Loading<T>(data: T? = null) : Resource<T>(data)
}

suspend fun <T> proceed(coroutine: suspend () -> T): Resource<T> {
    return try {
        Resource.Success(coroutine.invoke())
    } catch (exception: Exception) {
        when (exception) {
            is HttpException -> {
                val errorMessageResponseType = object : TypeToken<ResponseError>() {}.type
                val error: ResponseError = Gson().fromJson(
                    exception.response()?.errorBody()?.charStream(),
                    errorMessageResponseType
                )
                Resource.Error(exception, error.message)
            }
            else -> {
                Resource.Error(exception, exception.message)
            }
        }
    }
}