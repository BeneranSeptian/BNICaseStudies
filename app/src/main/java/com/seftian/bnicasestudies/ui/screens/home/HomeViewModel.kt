package com.seftian.bnicasestudies.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seftian.bnicasestudies.core.domain.ResourceState
import com.seftian.bnicasestudies.core.domain.model.PromoItem
import com.seftian.bnicasestudies.core.domain.model.Transaction
import com.seftian.bnicasestudies.core.domain.usecase.promo.PromoUseCase
import com.seftian.bnicasestudies.core.domain.usecase.saldo.SaldoUseCase
import com.seftian.bnicasestudies.core.domain.usecase.transaction.TransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val saldoUseCase: SaldoUseCase,
    private val promoUseCase: PromoUseCase,
    private val trxUseCase: TransactionUseCase
): ViewModel() {

    init {
        viewModelScope.launch {
            try {
                _saldo.first()
            }catch (e: Exception){
                saldoUseCase.updateSaldo(100000)
            }
        }
    }

    init {
        viewModelScope.launch {
            promoUseCase.getPromos().collectLatest {
                _promos.value = it
            }
        }
    }

    private val _promos = MutableStateFlow<ResourceState<List<PromoItem>>>(ResourceState.Loading)
    val promos get() = _promos

    private val _saldo: Flow<Long> = saldoUseCase.getSaldo()
    val saldo get() = _saldo

    private val _allTrx: Flow<List<Transaction>> = trxUseCase.getAllTrx()
    val allTrx get() = _allTrx

}