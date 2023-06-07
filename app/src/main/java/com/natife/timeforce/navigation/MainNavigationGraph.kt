package com.natife.timeforce.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.natife.timeforce.ui.screens.mainscreen.MainScreenNavigationView

@Composable
fun MainNavigationGraph() {
    val navController = rememberNavController()
    val bottomNavController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.MainScreen.route
    ) {
        composable(route = Screen.MainScreen.route) {
            MainScreenNavigationView(bottomNavController)
        }
    }
}