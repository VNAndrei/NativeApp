package ro.ubbcluj.scs.andreiverdes.beerkeeper.infrastructure

import java.lang.Exception

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception):Result<Nothing>()
    object Loading:Result<Nothing>()

    val Result<*>.succeeded
        get() = this is Result.Success && data != null

    val Result<*>.failed
        get() = this is Result.Error

    val Result<*>.loading
        get() = this is Result.Loading

}