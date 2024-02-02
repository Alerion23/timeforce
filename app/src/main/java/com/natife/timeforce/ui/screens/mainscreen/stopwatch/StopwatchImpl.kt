package com.natife.timeforce.ui.screens.mainscreen.stopwatch

import android.util.Log
import com.natife.timeforce.model.Lap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Timer
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration.Companion.milliseconds

@Singleton
class StopwatchImpl @Inject constructor() : Stopwatch {

    private val stopwatchState = MutableStateFlow(StopwatchState.Idle)
    private val time = MutableStateFlow(DEFAULT_TIME)
    private val lapList = MutableStateFlow<ArrayList<Lap>>(arrayListOf())
    private var lap = ZERO
    private var lapRound = 0
    private var duration = ZERO
    private var timeLap = ZERO
    private var timer: Timer? = null

    override fun execute() {
        when (stopwatchState.value) {
            StopwatchState.Idle -> {
                startStopwatch()
            }

            StopwatchState.Started -> {
                stopStopwatch()
            }

            StopwatchState.Stopped -> {
                startStopwatch()
            }
        }
    }

    override fun cancel() {
        timer?.cancel()
        duration = ZERO
        timeLap = ZERO
        lapRound = 0
        lapList.value = arrayListOf()
        stopwatchState.value = StopwatchState.Idle
    }

    override fun lap() {
        val list = lapList.value
        lapRound += 1
        lap = duration - timeLap
        timeLap = duration
        lap.toComponents { h, m, s, ns ->
            val time = "%02d:%02d:%02d:%02d".format(h.toInt(), m, s, ns / 10000000)
            list.add(Lap(lapRound, time))
            lapList.value = list
        }
        Log.d("~~~", "${lapList.value}")
    }

    private fun stopStopwatch() {
        timer?.cancel()
        lap()
        stopwatchState.value = StopwatchState.Stopped
    }

    private fun startStopwatch() {
        timer = fixedRateTimer(initialDelay = 1L, period = 1L) {
            duration = duration.plus(1.milliseconds)
            updateTimeUnits()
        }
        stopwatchState.value = StopwatchState.Started
    }

    private fun updateTimeUnits() {
        duration.toComponents { hours, minutes, seconds, nanoseconds ->
            val h = hours.toInt()
            val time = "%02d:%02d:%02d:%02d"
            val finalTime = time.format(
                h, minutes, seconds, nanoseconds / 10000000
            )
            this.time.value = finalTime
        }
    }

    override fun getTime(): Flow<String> = time

    override fun getStopwatchState(): Flow<StopwatchState> = stopwatchState

    override fun getLapTime(): Flow<List<Lap>> = lapList

    companion object {
        const val DEFAULT_TIME = "00:00:00:00"
    }
}