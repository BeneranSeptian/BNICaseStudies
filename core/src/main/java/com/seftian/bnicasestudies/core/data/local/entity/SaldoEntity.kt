package com.seftian.bnicasestudies.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "saldo_table"
)
data class SaldoEntity(
    @PrimaryKey
    val id: Int = 1,
    val saldo: Long = 0,
)