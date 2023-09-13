package com.seftian.bnicasestudies.core.domain.repository

import com.seftian.bnicasestudies.core.domain.ResourceState
import com.seftian.bnicasestudies.core.domain.model.PromoItem
import kotlinx.coroutines.flow.Flow

interface IPromoRepository {
    fun getPromos(): Flow<ResourceState<List<PromoItem>>>
    fun getPromo(id: Int): Flow<PromoItem>
}