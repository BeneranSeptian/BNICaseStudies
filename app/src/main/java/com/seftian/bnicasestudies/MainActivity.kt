package com.seftian.bnicasestudies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PieChart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.seftian.bnicasestudies.ui.BottomNavItem
import com.seftian.bnicasestudies.ui.Routes
import com.seftian.bnicasestudies.ui.screens.detail_promo.DetailPromoScreen
import com.seftian.bnicasestudies.ui.screens.home.HomeScreen
import com.seftian.bnicasestudies.ui.screens.payment_detail.PaymentDetailScreen
import com.seftian.bnicasestudies.ui.screens.portfolio.PortfolioScreen
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(
    navHostController: NavHostController = rememberNavController()
) {
    val bottomNavItems = listOf(
        BottomNavItem(
            label = "Home",
            route = Routes.Home.route,
            icon = Icons.Outlined.Home,
            iconSelected = Icons.Filled.Home
        ),
        BottomNavItem(
            label = "Portfolio",
            route = Routes.Portfolio.route,
            icon = Icons.Outlined.PieChart,
            iconSelected = Icons.Filled.PieChart,
        )
    )

    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    val currentBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val showNavBar = bottomNavItems.any{currentRoute == it.route}

    Scaffold(
        bottomBar = {
            if(showNavBar){
                NavigationBar {
                    bottomNavItems.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            onClick = {
                                selectedItemIndex = index
                                navHostController.navigate(item.route)
                            },
                            icon = {
                                Icon(imageVector = if(currentRoute == item.route) item.iconSelected else item.icon, contentDescription = item.label)
                            },
                            label = {Text(item.label)}
                        )
                    }
                }
            }
        }
    ) {
        NavHost(
            navController = navHostController,
            startDestination = Routes.Home.route,
            modifier = Modifier.padding(it)
        ) {
            composable(Routes.Home.route) {
                HomeScreen(navController = navHostController)
            }

            composable(Routes.PaymentDetail.route) { backStackEntry ->
                val idQr = backStackEntry.arguments?.getString("idQr")
                if (idQr != null) {
                    PaymentDetailScreen(idQr, navController = navHostController)
                } else {
                    Text("Error")
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

            composable(
                Routes.Portfolio.route
            ) {
                PortfolioScreen(
                    navController = navHostController
                )

            }
        }
    }
}