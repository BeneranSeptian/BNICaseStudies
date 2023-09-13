package com.seftian.bnicasestudies.core.domain.model

data class QRData(
    val bank: String,
    val transactionId: String,
    val merchantName: String,
    val transactionAmount: String
)
