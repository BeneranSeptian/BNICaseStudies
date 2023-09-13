package com.seftian.bnicasestudies.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.seftian.bnicasestudies.core.data.local.dao.PromoDao
import com.seftian.bnicasestudies.core.data.local.dao.SaldoDao
import com.seftian.bnicasestudies.core.data.local.dao.TransactionDao
import com.seftian.bnicasestudies.core.data.local.entity.PromoEntity
import com.seftian.bnicasestudies.core.data.local.entity.SaldoEntity
import com.seftian.bnicasestudies.core.data.local.entity.TrxEntity

@Database(
    entities = [SaldoEntity::class, TrxEntity::class, PromoEntity::class],
    version = 1
)
abstract class CaseStudiesAppDb: RoomDatabase() {
    abstract val saldoDao: SaldoDao
    abstract val trxDao: TransactionDao
    abstract val promoDao: PromoDao
}