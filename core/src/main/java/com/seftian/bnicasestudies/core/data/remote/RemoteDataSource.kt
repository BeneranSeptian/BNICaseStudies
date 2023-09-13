package com.seftian.bnicasestudies.core.data.remote

import com.seftian.bnicasestudies.core.data.remote.network.Digi46Api
import com.seftian.bnicasestudies.core.data.remote.network.ResponseStatus
import com.seftian.bnicasestudies.core.data.remote.response.PromoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val digi46Api: Digi46Api
) {

    fun getPromos(): Flow<ResponseStatus<PromoResponse>>{
        return safeApiCall { digi46Api.getPromos() }.flowOn(Dispatchers.IO)
    }

    private fun <T : Any> safeApiCall(
        apiCall: suspend () -> T
    ): Flow<ResponseStatus<T>> = flow {
        try {
            val response = apiCall()
            emit(ResponseStatus.Success(response))
        } catch (e: Exception) {
            emit(ResponseStatus.Error(e.message ?: "An error occurred"))
        }
    }
}