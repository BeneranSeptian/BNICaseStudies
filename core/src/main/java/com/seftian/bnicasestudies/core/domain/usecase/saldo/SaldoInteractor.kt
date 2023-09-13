package com.seftian.bnicasestudies.core.domain.usecase.saldo

import com.seftian.bnicasestudies.core.data.SaldoRepository
import com.seftian.bnicasestudies.core.domain.ResourceState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SaldoInteractor @Inject constructor(
    private val saldoRepository: SaldoRepository
): SaldoUseCase {
    override suspend fun updateSaldo(newSaldo: Long) {
        saldoRepository.updateSaldo(newSaldo)
    }

    override fun getSaldo(): Flow<Long> {
        return saldoRepository.getSaldo()
    }

    override suspend fun doTransaction(totalTrx: Long):Flow<ResourceState<Boolean>> {
        return flow {
            emit(ResourceState.Loading)
            delay(2000)
            val currentSaldo = saldoRepository.getSaldo().first()

            if(totalTrx > currentSaldo) {
                emit(ResourceState.Error("Saldo Kurang"))
            }else {
                val newSaldo = currentSaldo - totalTrx
                saldoRepository.updateSaldo(newSaldo)

                emit(ResourceState.Success(true))
            }
        }
    }
}