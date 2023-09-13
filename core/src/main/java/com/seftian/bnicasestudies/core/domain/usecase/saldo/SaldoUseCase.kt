package com.seftian.bnicasestudies.core.domain.usecase.saldo

import com.seftian.bnicasestudies.core.domain.ResourceState
import kotlinx.coroutines.flow.Flow

interface SaldoUseCase {
    suspend fun updateSaldo(newSaldo: Long)
    fun getSaldo(): Flow<Long>
    suspend fun doTransaction(totalTrx: Long):Flow<ResourceState<Boolean>>
}