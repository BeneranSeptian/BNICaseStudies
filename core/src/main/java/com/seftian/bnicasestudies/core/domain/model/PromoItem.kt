package com.seftian.bnicasestudies.core.domain.model

data class PromoItem(
    val createdAt: String,
    val desc: String,
    val id: Int,
    val img: Img,
    val lokasi: String,
    val nama: String,
)

data class Img(
    val url: String,
)
