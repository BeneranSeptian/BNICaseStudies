package com.seftian.bnicasestudies.core.domain.usecase.transaction

import com.seftian.bnicasestudies.core.data.TrxRepository
import com.seftian.bnicasestudies.core.domain.model.Transaction
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionInteractor @Inject constructor(
    private val trxRepository: TrxRepository
): TransactionUseCase {

    override fun getAllTrx(): Flow<List<Transaction>> {
        return trxRepository.getAllTransactions()
    }

    override suspend fun insertTrx(transaction: Transaction) {
        trxRepository.insertTrx(transaction)
    }

}