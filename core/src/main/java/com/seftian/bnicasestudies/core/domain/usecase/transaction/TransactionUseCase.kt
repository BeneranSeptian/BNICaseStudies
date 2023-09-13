package com.seftian.bnicasestudies.core.domain.usecase.transaction

import com.seftian.bnicasestudies.core.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionUseCase {
    fun getAllTrx(): Flow<List<Transaction>>
    suspend fun insertTrx(transaction: Transaction)
}