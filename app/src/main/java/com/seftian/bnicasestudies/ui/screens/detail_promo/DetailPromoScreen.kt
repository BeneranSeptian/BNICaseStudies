package com.seftian.bnicasestudies.ui.screens.detail_promo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.seftian.bnicasestudies.core.domain.model.PromoItem

@Composable
fun DetailPromoScreen(
    idPromo: Int,
    navController: NavController,
    viewModel: DetailPromoViewModel = hiltViewModel()
) {

    val promo by viewModel.promo.collectAsState()

    LaunchedEffect(key1 = idPromo){
        viewModel.getPromo(idPromo)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        promo?.let { Text(it.desc) }
    }
}