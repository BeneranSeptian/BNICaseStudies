package com.seftian.bnicasestudies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.seftian.bnicasestudies.core.domain.model.PromoItem
import com.seftian.bnicasestudies.ui.Routes
import com.seftian.bnicasestudies.ui.screens.detail_promo.DetailPromoScreen
import com.seftian.bnicasestudies.ui.screens.home.HomeScreen
import com.seftian.bnicasestudies.ui.screens.payment_detail.PaymentDetailScreen
import com.seftian.bnicasestudies.ui.screens.qr_scan.QrScreen
import com.seftian.bnicasestudies.ui.theme.BNICaseStudiesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BNICaseStudiesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainApp()
                }
            }
        }
    }
}

@Composable
fun MainApp(
    navHostController: NavHostController = rememberNavController()
) {
    NavHost(navController = navHostController, startDestination = Routes.Home.route) {

        composable(Routes.Home.route) {
            HomeScreen(navController = navHostController)
        }

        composable(Routes.PaymentDetail.route) { backStackEntry ->
            val idQr = backStackEntry.arguments?.getString("idQr")
            if (idQr != null) {
                PaymentDetailScreen(idQr, navController = navHostController)
            } else {
                Text("Error: No Game ID provided.")
            }
        }

        composable(Routes.QrScreen.route) {
            QrScreen(navController = navHostController)
        }

        composable(
            Routes.DetailPromo.route,
            arguments = listOf(navArgument("idPromo") { type = NavType.IntType })
        ) { backStackEntry ->
            val idPromo = backStackEntry.arguments?.getInt("idPromo")
            if (idPromo != null) {
                DetailPromoScreen(idPromo = idPromo, navController = navHostController)
            } else {
                Text("Error: data not valid")
            }
        }
    }
}