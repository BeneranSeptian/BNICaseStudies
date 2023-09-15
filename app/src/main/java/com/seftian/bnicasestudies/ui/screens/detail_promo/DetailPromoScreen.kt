package com.seftian.bnicasestudies.ui.screens.detail_promo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage

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

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        promo?.let {
            AsyncImage(
                model = it.img.url,
                contentDescription = "banner image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(it.lokasi)

            Spacer(modifier = Modifier.height(16.dp))
            Text(it.desc, fontSize = 12.sp)
        }
    }
}