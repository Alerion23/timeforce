package com.natife.timeforce.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.natife.timeforce.ui.screens.mainscreen.alarm.AlarmClockView
import com.natife.timeforce.ui.screens.mainscreen.clock.ClockView
import com.natife.timeforce.ui.screens.mainscreen.stopwatch.StopWatchView
import com.natife.timeforce.ui.screens.mainscreen.timer.TimerView

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.AlarmClock.screenRoute
    ) {
        composable(route = BottomNavItem.AlarmClock.screenRoute) {
            AlarmClockView()
        }
        composable(route = BottomNavItem.Clock.screenRoute) {
            ClockView()
        }
        composable(route = BottomNavItem.StopWatch.screenRoute) {
            StopWatchView()
        }
        composable(route = BottomNavItem.Timer.screenRoute) {
            TimerView()
        }
    }
}