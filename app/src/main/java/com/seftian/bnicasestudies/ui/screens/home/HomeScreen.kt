package com.seftian.bnicasestudies.ui.screens.home

import android.Manifest
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.seftian.bnicasestudies.core.domain.ResourceState
import com.seftian.bnicasestudies.core.domain.model.PromoItem
import com.seftian.bnicasestudies.core.domain.model.Transaction
import com.seftian.bnicasestudies.ui.Routes
import com.seftian.bnicasestudies.util.Helper

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavController
) {

    val saldo by viewModel.saldo.collectAsState(initial = 0)
    val promos by viewModel.promos.collectAsState(initial = ResourceState.Loading)
    val allTrx by viewModel.allTrx.collectAsState(initial = emptyList())


    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    val cameraPermissionGranted = cameraPermissionState.status.isGranted
    var hasRequestedPermission by remember { mutableStateOf(false) }

    LaunchedEffect(cameraPermissionState.status) {
        if (hasRequestedPermission && cameraPermissionState.status.isGranted) {
            navController.navigate(Routes.QrScreen.route)
            hasRequestedPermission = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().height(200.dp),
            contentAlignment = Alignment.Center
        ){
            when (promos) {
                is ResourceState.Error -> {
                    Text("gagal memuat promosi")
                }

                is ResourceState.Success -> {
                    val promoList = (promos as ResourceState.Success).data
                    BannerComponent(
                        bannerItemList = promoList, onClick = {
                            navController.navigate(Routes.DetailPromo.withData(it.id))
                        })
                }

                ResourceState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        SaldoSection(
            onScanClick = {
                if (!cameraPermissionGranted) {
                    hasRequestedPermission = true
                    cameraPermissionState.launchPermissionRequest()
                } else {
                    navController.navigate(Routes.QrScreen.route)
                }
            },
            onTopUpClick = { },
            saldoText = Helper.convertLongToCurrencyString(saldo)
        )

        Spacer(modifier = Modifier.height(24.dp))

        if(allTrx.isNotEmpty()){
            HistoryTransactionSection(trxList = allTrx)
        }
    }
}

@Composable
fun SaldoText(
    modifier: Modifier = Modifier,
    saldoText: String,
    fontSize: TextUnit = 18.sp,
    fontWeight: FontWeight = FontWeight.Bold
) {
    Text(saldoText, modifier = modifier, fontSize = fontSize, fontWeight = fontWeight)
}

@Composable
fun BannerImage(
    imageUrl: String,
    modifier: Modifier = Modifier.fillMaxSize(),
    onClick: () -> Unit
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = "banner image",
        contentScale = ContentScale.Fit,
        modifier = modifier.clickable {
            onClick()
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BannerComponent(
    modifier: Modifier = Modifier,
    bannerItemList: List<PromoItem>,
    onClick: (PromoItem) -> Unit
) {
    val pagerState = rememberPagerState(
        pageCount = { bannerItemList.size }
    )

    Box(
        modifier = modifier
            .height(200.dp)
            .fillMaxWidth()
    ) {
        HorizontalPager(state = pagerState) {
            BannerImage(imageUrl = bannerItemList[it].img.url, onClick = {
                onClick(bannerItemList[it])
            })
        }

        Row(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(bannerItemList.size) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(10.dp)
                )
            }
        }
    }
}

@Composable
fun SaldoSection(
    modifier: Modifier = Modifier,
    onScanClick: () -> Unit,
    onTopUpClick: () -> Unit,
    saldoText: String
) {
    Row(
        modifier = modifier
            .height(80.dp)
            .fillMaxWidth()
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconWithLabel(
            labelText = "Scan QR",
            icon = Icons.Outlined.QrCodeScanner,
            contentDescription = "scan qr",
            onClick = {
                onScanClick()
            }
        )

        SaldoText(saldoText = saldoText)

        IconWithLabel(
            labelText = "Top Up",
            icon = Icons.Outlined.FileUpload,
            contentDescription = "top up",
            onClick = { onTopUpClick() }
        )
    }
}

@Composable
fun IconWithLabel(
    labelText: String,
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.clickable(onClick = { onClick() })
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(labelText)
    }
}

@Composable
fun HistoryTransactionSection(
    modifier: Modifier = Modifier,
    trxList: List<Transaction>
) {

    Text("Riwayat Transaksi", fontWeight = FontWeight.Bold, fontSize = 16.sp)
    Spacer(modifier = Modifier.height(16.dp))

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(trxList) {
            TransactionItem(transaction = it)
        }
    }
}

@Composable
fun TransactionItem(
    transaction: Transaction,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(width = 1.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Text(text = transaction.id)
        Text(text = transaction.merchantName)
        Text(text = Helper.convertLongToCurrencyString(transaction.trxAmount))
    }
}