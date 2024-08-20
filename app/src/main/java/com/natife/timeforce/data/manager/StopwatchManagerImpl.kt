package com.natife.timeforce.data.manager

import com.natife.timeforce.model.Lap
import com.natife.timeforce.ui.screens.mainscreen.stopwatch.StopwatchState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StopwatchManagerImpl @Inject constructor() : StopwatchManager {

    private val _stopwatchState = MutableStateFlow(StopwatchState.Idle)
    private val _time = MutableStateFlow(DEFAULT_STOPWATCH_TIME)
    private val _notificationTime = MutableStateFlow(DEFAULT_NOTIFICATION_TIME)
    private val _lapList = MutableStateFlow<List<Lap>>(emptyList())

    private var lappedTime = 0L
    private var lap = 0L
    private var lapRound = 0
    private var seconds = 0L
    private var startTime = 0L
    private var pausedTime = 0L
    private var stopwatchJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    override suspend fun start() {
        withContext(Dispatchers.Default) {
            startTime = if (pausedTime == 0L) {
                System.currentTimeMillis()
            } else {
                System.currentTimeMillis() - pausedTime
            }
            _stopwatchState.value = StopwatchState.Started
            stopwatchJob = scope.launch {
                while (isActive) {
                    val currentTime = System.currentTimeMillis()
                    val elapsed = currentTime - startTime
                    _time.value = formatTime(elapsed)
                    delay(10L)
                }
            }
        }
    }

    override suspend fun stop() {
        stopwatchJob?.cancel()
        pausedTime = System.currentTimeMillis() - startTime
        _stopwatchState.value = StopwatchState.Stopped
    }

    override suspend fun reset() {
        stopwatchJob?.cancel()
        val list = _lapList.value.toMutableList()
        startTime = 0L
        pausedTime = 0L
        lapRound = 0
        list.clear()
        _lapList.value = list
        _time.value = DEFAULT_STOPWATCH_TIME
        _stopwatchState.value = StopwatchState.Idle
    }

    override suspend fun getTime(): Flow<String> {
        return _time
    }

    override suspend fun getStopwatchState(): Flow<StopwatchState> {
        return _stopwatchState
    }

    override suspend fun getNotificationTime(): Flow<String> {
        return _notificationTime
    }

    override suspend fun lap() {
        withContext(Dispatchers.Default) {
            val list = _lapList.value.toMutableList()
            val currentTime = System.currentTimeMillis()
            lap = if (lapRound == 0) {
                currentTime - startTime
            } else {
                currentTime - lappedTime
            }
            lappedTime = currentTime
            lapRound++
            val lapItem = Lap(lapRound, lapCount = lapRound, lap = formatTime(lap))
            list.add(0, lapItem)
            _lapList.value = list
        }
    }

    override suspend fun getLapList(): Flow<List<Lap>> {
        return _lapList
    }

    private fun formatTime(ms: Long): String {
        val hours = (ms / 3600000) % 24
        val minutes = (ms / 60000) % 60
        val seconds = (ms / 1000) % 60
        val centiseconds = (ms / 10) % 100
        if (this.seconds != seconds) {
            _notificationTime.value = DEFAULT_NOTIFICATION_FORMAT.format(hours, minutes, seconds)
            this.seconds = seconds
        }
        return DEFAULT_FORMAT.format(hours, minutes, seconds, centiseconds)
    }

    companion object {
        const val DEFAULT_STOPWATCH_TIME = "00:00:00:00"
        const val DEFAULT_NOTIFICATION_TIME = "00:00:00"
        const val DEFAULT_FORMAT = "%02d:%02d:%02d:%02d"
        const val DEFAULT_NOTIFICATION_FORMAT = "%02d:%02d:%02d"
    }
}