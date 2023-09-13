package com.seftian.bnicasestudies.core.domain

sealed class ResourceState<out T: Any>{
    data class Success<out T : Any>(val data: T) : ResourceState<T>()
    data class Error(val message: String?) : ResourceState<Nothing>()
    object Loading : ResourceState<Nothing>()
}