package com.seftian.bnicasestudies.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.seftian.bnicasestudies.core.data.local.entity.PromoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PromoDao {

    @Query("SELECT * from promos")
    fun getAllPromo(): Flow<List<PromoEntity>>

    @Query("SELECT * from promos WHERE id = :id")
    fun getSinglePromo(id: Int): Flow<PromoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPromo(promoEntity: PromoEntity)
}