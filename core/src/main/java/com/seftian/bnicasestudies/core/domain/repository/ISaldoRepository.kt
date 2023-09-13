package com.seftian.bnicasestudies.core.domain.repository


import kotlinx.coroutines.flow.Flow

interface ISaldoRepository {
    fun getSaldo(): Flow<Long>
    suspend fun updateSaldo(newSaldo: Long)
}