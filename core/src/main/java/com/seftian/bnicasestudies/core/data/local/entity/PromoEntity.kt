package com.seftian.bnicasestudies.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "promos"
)
data class PromoEntity(
    @PrimaryKey
    val id: Int,
    val createdAt: String,
    val desc: String,
    val url: String,
    val lokasi: String,
    val nama: String,
)