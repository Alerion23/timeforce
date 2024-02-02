package com.natife.timeforce.ui.screens.mainscreen.stopwatch

import com.natife.timeforce.model.Lap
import kotlinx.coroutines.flow.Flow

interface Stopwatch {

    fun execute()
    fun cancel()
    fun lap()
    fun getTime(): Flow<String>
    fun getStopwatchState(): Flow<StopwatchState>
    fun getLapTime(): Flow<List<Lap>>

}