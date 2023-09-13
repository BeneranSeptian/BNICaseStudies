package com.seftian.bnicasestudies.core.data

import com.seftian.bnicasestudies.core.data.local.LocalDataSource
import com.seftian.bnicasestudies.core.domain.repository.ISaldoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SaldoRepository @Inject constructor(
    private val localDataSource: LocalDataSource
): ISaldoRepository {

    override fun getSaldo(): Flow<Long> {
        return localDataSource.getSaldoFromLocal()
    }

    override suspend fun updateSaldo(newSaldo: Long) {
        withContext(Dispatchers.IO){
            localDataSource.updateSaldoInLocal(newSaldo)
        }
    }
}