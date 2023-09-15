package com.seftian.bnicasestudies.ui.screens.detail_promo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seftian.bnicasestudies.core.domain.model.PromoItem
import com.seftian.bnicasestudies.core.domain.usecase.promo.PromoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailPromoViewModel @Inject constructor(
    private val promoUseCase: PromoUseCase
) :ViewModel() {

    private val _promo = MutableStateFlow<PromoItem?>(null)
    val promo get() = _promo

    fun getPromo(id: Int){
        viewModelScope.launch {
            promoUseCase.getPromo(id).collectLatest {
                _promo.value = it
            }
        }
    }
}