package com.seftian.bnicasestudies.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.seftian.bnicasestudies.core.data.local.entity.SaldoEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface SaldoDao {
    @Query(
        "SELECT saldo from saldo_table"
    )
    fun getSaldo(): Flow<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateSaldo(saldoEntity: SaldoEntity)
}