package com.seftian.bnicasestudies.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "transaction"
)
data class TrxEntity (
    @PrimaryKey(autoGenerate = true)
    val idTrx: Int=0,
    val id: String,
    val sourceBank: String,
    val merchantName: String,
    val trxAmount: String,
    val createdAt: Long = System.currentTimeMillis()
)