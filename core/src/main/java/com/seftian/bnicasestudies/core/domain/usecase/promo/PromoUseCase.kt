package com.seftian.bnicasestudies.core.domain.usecase.promo

import com.seftian.bnicasestudies.core.domain.ResourceState
import com.seftian.bnicasestudies.core.domain.model.PromoItem
import kotlinx.coroutines.flow.Flow

interface PromoUseCase {
    fun getPromos(): Flow<ResourceState<List<PromoItem>>>
    fun getPromo(id: Int): Flow<PromoItem>
}