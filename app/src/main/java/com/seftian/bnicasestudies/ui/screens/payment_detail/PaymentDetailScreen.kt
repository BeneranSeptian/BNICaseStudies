package com.seftian.bnicasestudies.ui.screens.payment_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.seftian.bnicasestudies.core.domain.ResourceState
import com.seftian.bnicasestudies.core.domain.model.Transaction
import com.seftian.bnicasestudies.util.Helper

@Composable
fun PaymentDetailScreen(
    idQr: String,
    viewModel: PaymentDetailViewModel = hiltViewModel(),
    navController: NavController
) {
    val qrData = Helper.parseQRString(idQr)
    val saldo by viewModel.saldo.collectAsState(initial = 0)
    val transactionStatus by viewModel.transactionStatus.collectAsState()
    var showPopUp by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center // This ensures the inner Column is centered vertically
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            qrData?.let {
                TransactionCard(
                    merchantName = it.merchantName,
                    trxId = it.transactionId,
                    trxAmount = "Rp${it.transactionAmount}",
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Saldo Anda", fontWeight = FontWeight.Bold)
                    Text(Helper.convertLongToCurrencyString(saldo), fontWeight = FontWeight.Bold)
                }
            }
        }

        Button(
            onClick = {

                showPopUp = true
                qrData?.transactionAmount?.toLong()?.let {
                    viewModel.doTransaction(it)
                }

                qrData?.let {
                    val trx = Transaction(
                        id = it.transactionId,
                        sourceBank = it.bank,
                        merchantName = it.merchantName,
                        trxAmount = it.transactionAmount.toLong()
                    )
                    viewModel.insertTrxToLocal(trx)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Bayar")
        }

        if (showPopUp) {
            TransactionStatusPopUp(
                transactionStatus = transactionStatus,
                onClose = {
                    showPopUp = false
                    navController.popBackStack()
                },
            )
        }

    }
}


@Composable
fun TransactionCard(
    modifier: Modifier = Modifier,
    merchantName: String,
    trxId: String,
    trxAmount: String,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = merchantName,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = trxId)

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = trxAmount)
        }
    }
}

@Composable
fun TransactionStatusPopUp(transactionStatus: ResourceState<Boolean>?, onClose: () -> Unit) {
    AlertDialog(
        onDismissRequest = onClose,
        title = {
            when (transactionStatus) {
                is ResourceState.Error -> Text("Error")
                is ResourceState.Loading -> Text("Loading")
                is ResourceState.Success -> Text("Success")
                else -> Text("Unknown")
            }
        },
        text = {
            when (transactionStatus) {
                is ResourceState.Error -> (transactionStatus as ResourceState.Error).message?.let {
                    Text(
                        it
                    )
                }

                is ResourceState.Loading -> CircularProgressIndicator(modifier = Modifier.height(80.dp))
                is ResourceState.Success -> {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.Green,
                        modifier = Modifier.size(80.dp)
                    )
                }

                else -> {}
            }
        },
        confirmButton = {
            when (transactionStatus) {
                is ResourceState.Error -> {
                    TextButton(onClick = onClose) {
                        Text("Close")
                    }
                }
                is ResourceState.Loading -> {}
                is ResourceState.Success -> {
                    TextButton(onClick = onClose) {
                        Text("Close")
                    }
                }

                else -> {}
            }
        }
    )
}