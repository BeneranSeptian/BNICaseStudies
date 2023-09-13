package com.seftian.bnicasestudies.core.data.remote.network

sealed class ResponseStatus<out T: Any>{
    data class Success<out T : Any>(val data: T) : ResponseStatus<T>()
    data class Error(val message: String?) : ResponseStatus<Nothing>()
}
