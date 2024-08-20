package com.natife.timeforce.ui.navigation.bottom

import com.natife.timeforce.R

sealed class BottomNavItem(var title: Int, var icon: Int, var screenRoute: String) {

    data object AlarmClock :
        BottomNavItem(R.string.alarm_title, R.drawable.icon_alarm, "alarm clock screen")

    data object Clock : BottomNavItem(R.string.clock_title, R.drawable.icon_clock, "clock screen")
    data object Timer : BottomNavItem(R.string.timer_title, R.drawable.icon_timer, "timer screen")
    data object StopWatch :
        BottomNavItem(R.string.stopwatch_title, R.drawable.icon_stopwatch, "stopwatch screen")
}
