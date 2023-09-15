package com.seftian.bnicasestudies.core.data.local

import com.seftian.bnicasestudies.core.data.local.dao.PromoDao
import com.seftian.bnicasestudies.core.data.local.dao.SaldoDao
import com.seftian.bnicasestudies.core.data.local.dao.TransactionDao
import com.seftian.bnicasestudies.core.data.local.entity.PromoEntity
import com.seftian.bnicasestudies.core.data.local.entity.SaldoEntity
import com.seftian.bnicasestudies.core.data.local.entity.TrxEntity
import kotlinx.coroutines.flow.Flow

class LocalDataSource(
    private val saldoDao: SaldoDao,
    private val trxDao: TransactionDao,
    private val promoDao: PromoDao
) {

    fun getSaldoFromLocal(): Flow<Long> = saldoDao.getSaldo()

    fun updateSaldoInLocal(newSaldo: Long){
        val saldo = SaldoEntity(
            id = 1,
            saldo = newSaldo
        )
        saldoDao.updateSaldo(saldo)
    }

    fun getAllTrx(): Flow<List<TrxEntity>> = trxDao.getAllTransaction()

    fun insertTrx(trxEntity: TrxEntity){
        trxDao.insertTrx(trxEntity)
    }

    fun insertPromo(promoEntity: PromoEntity){
        promoDao.insertPromo(promoEntity)
    }

    fun getAllPromo(): Flow<List<PromoEntity>> = promoDao.getAllPromo()

    fun getSinglePromo(id: Int) = promoDao.getSinglePromo(id)
}