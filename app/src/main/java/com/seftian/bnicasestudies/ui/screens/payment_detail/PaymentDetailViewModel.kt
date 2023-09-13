package com.seftian.bnicasestudies.ui.screens.payment_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seftian.bnicasestudies.core.domain.ResourceState
import com.seftian.bnicasestudies.core.domain.model.Transaction
import com.seftian.bnicasestudies.core.domain.usecase.saldo.SaldoUseCase
import com.seftian.bnicasestudies.core.domain.usecase.transaction.TransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentDetailViewModel @Inject constructor(
    private val saldoUseCase: SaldoUseCase,
    private val transactionUseCase: TransactionUseCase
): ViewModel() {

    private val _saldo: Flow<Long> = saldoUseCase.getSaldo()
    val saldo get() = _saldo

    private val _transactionStatus = MutableStateFlow<ResourceState<Boolean>?>(null)
    val transactionStatus get() = _transactionStatus

    fun doTransaction(totalTrx: Long){
        viewModelScope.launch {
            saldoUseCase.doTransaction(totalTrx).collectLatest {
                _transactionStatus.value = it
            }
        }
    }

    fun insertTrxToLocal(transaction: Transaction){
        viewModelScope.launch {
            transactionUseCase.insertTrx(transaction)
        }
    }
}