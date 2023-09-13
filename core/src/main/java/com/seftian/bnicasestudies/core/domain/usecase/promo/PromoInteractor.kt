package com.seftian.bnicasestudies.core.domain.usecase.promo

import com.seftian.bnicasestudies.core.data.PromoRepository
import com.seftian.bnicasestudies.core.domain.ResourceState
import com.seftian.bnicasestudies.core.domain.model.PromoItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class PromoInteractor @Inject constructor(
    private val promoRepository: PromoRepository
): PromoUseCase {

    override fun getPromos(): Flow<ResourceState<List<PromoItem>>> {
        return promoRepository.getPromos()
    }

    override fun getPromo(id: Int): Flow<PromoItem> {
        return promoRepository.getPromo(id)
    }

}