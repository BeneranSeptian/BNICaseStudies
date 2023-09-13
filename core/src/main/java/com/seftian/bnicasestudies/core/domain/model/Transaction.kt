package com.seftian.bnicasestudies.core.domain.model

data class Transaction(
    val id: String,
    val sourceBank: String,
    val merchantName: String,
    val trxAmount: String,
)
