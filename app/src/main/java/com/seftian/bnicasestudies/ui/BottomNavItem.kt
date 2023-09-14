package com.seftian.bnicasestudies.ui

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: ImageVector,
    val iconSelected: ImageVector
)
