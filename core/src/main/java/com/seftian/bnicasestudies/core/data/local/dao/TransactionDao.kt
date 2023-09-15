package com.seftian.bnicasestudies.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.seftian.bnicasestudies.core.data.local.entity.TrxEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface TransactionDao {
    @Query(
        "SELECT * from `transaction`"
    )
    fun getAllTransaction(): Flow<List<TrxEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrx(trxEntity: TrxEntity)
}