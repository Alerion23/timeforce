package com.natife.timeforce.data.manager

import com.natife.timeforce.model.Lap
import com.natife.timeforce.ui.screens.mainscreen.stopwatch.StopwatchState
import kotlinx.coroutines.flow.Flow

interface StopwatchManager {

    suspend fun start()
    suspend fun stop()
    suspend fun reset()
    suspend fun lap()
    suspend fun getTime(): Flow<String>
    suspend fun getStopwatchState(): Flow<StopwatchState>
    suspend fun getNotificationTime(): Flow<String>
    suspend fun getLapList(): Flow<List<Lap>>
}