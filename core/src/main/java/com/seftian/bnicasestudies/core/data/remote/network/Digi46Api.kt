package com.seftian.bnicasestudies.core.data.remote.network

import com.seftian.bnicasestudies.core.data.remote.response.PromoResponse
import retrofit2.http.GET

interface Digi46Api {
    @GET("promos")
    suspend fun getPromos(): PromoResponse

    companion object {
        const val BASE_URL = "https://content.digi46.id/"
    }
}