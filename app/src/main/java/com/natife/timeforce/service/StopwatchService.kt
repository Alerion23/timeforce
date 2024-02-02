package com.natife.timeforce.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.util.Timer
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class StopwatchService : Service() {

    private var isRunning = false
    private var duration = Duration.ZERO
    private var msDuration = Duration.ZERO
    private var timer: Timer? = null
    private var msTimer: Timer? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startStopwatch()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun updateTimeUnits() {
        duration.toComponents { hours, minutes, seconds, _ ->
            val h = hours.toInt()
            val time = "%02d:%02d:%02d"
            val finalTime = time.format(h, minutes, seconds)
            val intent = Intent(UPDATE_TIME)
            intent.putExtra("time", finalTime)
            sendBroadcast(intent)
        }
    }

    private fun startStopwatch() {
        msTimer = fixedRateTimer(initialDelay = 10L, period = 10L) {
            msDuration = msDuration.plus(1.milliseconds)
            updateMsTimeUnit()
        }
        timer = fixedRateTimer(initialDelay = 10L, period = 1000L) {
            duration = duration.plus(1.seconds)
            updateTimeUnits()
        }
    }

    private fun updateMsTimeUnit() {
        val ms = msDuration.inWholeMilliseconds % 100
        val time = ":%02d".format(ms)
        val intent = Intent(UPDATE_TIME_MS)
        intent.putExtra("ms_time", time)
        sendBroadcast(intent)
    }

    companion object {
        private const val DEFAULT_VALUE = "0"
        const val UPDATE_TIME = "update_time"
        const val UPDATE_TIME_MS = "update_ms_time"
    }

}