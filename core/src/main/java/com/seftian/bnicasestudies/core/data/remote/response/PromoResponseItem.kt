package com.seftian.bnicasestudies.core.data.remote.response

data class PromoResponseItem(
    val createdAt: String,
    val desc: String,
    val id: Int,
    val img: ImgResponse,
    val lokasi: String,
    val nama: String,
)