package com.natife.timeforce.ui.navigation.bottom

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.natife.timeforce.ui.navigation.main.MainScreenNavigationView

@Composable
fun MainNavigationGraph() {
    val navController = rememberNavController()
    val bottomNavController = rememberNavController()
    with(navController) {
        NavHost(
            navController = this,
            startDestination = Screen.MainScreen.route
        ) {
            composable(route = Screen.MainScreen.route) {
                MainScreenNavigationView(bottomNavController)
            }
        }
    }

}