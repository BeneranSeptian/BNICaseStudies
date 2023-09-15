package com.seftian.bnicasestudies.core.data

import com.seftian.bnicasestudies.core.data.local.LocalDataSource
import com.seftian.bnicasestudies.core.data.local.entity.TrxEntity
import com.seftian.bnicasestudies.core.domain.model.Transaction
import com.seftian.bnicasestudies.core.domain.repository.ITrxRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TrxRepository @Inject constructor(
    private val localDataSource: LocalDataSource
): ITrxRepository {
    override fun getAllTransactions(): Flow<List<Transaction>> {
        return flow {
            val allTrx = localDataSource.getAllTrx().first().map {
                Transaction(
                    id = it.id,
                    sourceBank = it.sourceBank,
                    merchantName = it.merchantName,
                    trxAmount = it.trxAmount
                )
            }
            emit(allTrx)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun insertTrx(transaction: Transaction) {
        withContext(Dispatchers.IO){

            val trxEntity = TrxEntity(
                id = transaction.id,
                sourceBank = transaction.sourceBank,
                trxAmount = transaction.trxAmount,
                merchantName = transaction.merchantName
            )
            localDataSource.insertTrx(trxEntity)
        }
    }
}