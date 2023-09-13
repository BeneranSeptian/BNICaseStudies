package com.seftian.bnicasestudies.core.domain.repository

import com.seftian.bnicasestudies.core.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface ITrxRepository {
    fun getAllTransactions(): Flow<List<Transaction>>
    suspend fun insertTrx(transaction: Transaction)
}