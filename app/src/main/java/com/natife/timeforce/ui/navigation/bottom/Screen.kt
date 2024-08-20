package com.natife.timeforce.ui.navigation.bottom

sealed class Screen(val route: String) {
    data object MainScreen : Screen("main_screen")
}
