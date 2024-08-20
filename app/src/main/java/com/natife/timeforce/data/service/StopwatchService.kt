package com.natife.timeforce.data.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.natife.timeforce.R
import com.natife.timeforce.data.manager.StopwatchManager
import com.natife.timeforce.ui.screens.mainscreen.stopwatch.StopwatchState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StopwatchService : Service() {

    @Inject
    lateinit var stopwatchManager: StopwatchManager

    private var notification: NotificationCompat.Builder? = null
    private var notificationManager: NotificationManagerCompat? = null
    private var startJob: Job? = null
    private var stopwatchJob: Job? = null
    private var resetJob: Job? = null
    private var lapJob: Job? = null
    private var observingJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main + Job())

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_STOPWATCH_FOREGROUND -> {
                startStopwatchNotification()
            }

            ACTION_START -> startStopwatch()
            ACTION_LAP -> lapStopwatch()
            ACTION_STOP -> stopStopwatch()
            ACTION_RESET -> resetStopwatch()
        }
        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopForeground(STOP_FOREGROUND_DETACH)
        stopwatchJob?.cancel()
        observingJob?.cancel()
        startJob?.cancel()
        stopwatchJob?.cancel()
        resetJob?.cancel()
        lapJob?.cancel()
    }

    private fun lapStopwatch() {
        lapJob = scope.launch {
            stopwatchManager.lap()
        }
    }

    private fun startStopwatch() {
        stopwatchJob = scope.launch {
            stopwatchManager.start()
        }
    }

    private fun resetStopwatch() {
        resetJob = scope.launch {
            stopwatchManager.reset()
            stopForeground(STOP_FOREGROUND_DETACH)
        }
    }

    private fun stopStopwatch() {
        stopwatchJob = scope.launch {
            stopwatchManager.stop()
        }
    }

    private fun startStopwatchNotification() {
        startJob = scope.launch {
            if (notificationManager == null) {
                firstStart()
            }
            if (notification != null) {
                startForeground(NOTIFICATION_ID, notification?.build())
                observingJob = scope.launch {
                    observe()
                }
            }
        }
    }

    private suspend fun firstStart() {
        notificationManager = NotificationManagerCompat.from(this@StopwatchService)
        createNotificationChannel()
        val time = stopwatchManager.getNotificationTime().first()
        val primaryState = stopwatchManager.getStopwatchState().first()
        if (primaryState != StopwatchState.Idle) {
            val action = when (primaryState) {
                StopwatchState.Started -> ACTION_STOP
                StopwatchState.Stopped -> ACTION_START
                else -> ACTION_START
            }
            createNotification(action, time)
        }
    }

    private suspend fun observe() {
        combine(
            stopwatchManager.getStopwatchState(),
            stopwatchManager.getNotificationTime()
        ) { state, time ->
            state to time
        }.collectLatest {
            val action = when (it.first) {
                StopwatchState.Started -> ACTION_STOP
                StopwatchState.Stopped -> ACTION_START
                else -> ACTION_START
            }
            createNotification(action, it.second)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                notification?.let { notif ->
                    notificationManager?.notify(NOTIFICATION_ID, notif.build())
                }
            }
        }
    }

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            getString(R.string.app_name),
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager?.createNotificationChannel(serviceChannel)
    }

    private fun createActionState(action: String): NotificationCompat.Action {
        val actionText: String
        val code: Int
        val notificationIntent = Intent(this, StopwatchService::class.java).apply {
            this.action = action
        }
        when (action) {
            ACTION_START -> {
                code = ACTION_START_STOP_ID
                actionText = getString(R.string.start)
            }

            ACTION_STOP -> {
                code = ACTION_START_STOP_ID
                actionText = getString(R.string.stop)
            }

            ACTION_LAP -> {
                code = ACTION_LAP_ID
                actionText = getString(R.string.lap)
            }

            else -> {
                code = ACTION_START_STOP_ID
                actionText = getString(R.string.start)
            }
        }
        val pendingIntentState = PendingIntent.getService(
            this,
            code,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Action.Builder(
            0,
            actionText,
            pendingIntentState
        ).build()
    }

    private fun createNotification(stopwatchAction: String, time: String) {
        val action = createActionState(stopwatchAction)
        val actionLap = createActionState(ACTION_LAP)
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(getString(R.string.stopwatch))
            .setContentText(time)
            .setSmallIcon(R.drawable.icon_stopwatch)
            .addAction(action)
            .addAction(actionLap)
            .setOngoing(true)
        notification = notificationBuilder
    }

    override fun onDestroy() {
        super.onDestroy()
        startJob?.cancel()
        observingJob?.cancel()
        stopwatchJob?.cancel()
        resetJob?.cancel()
        lapJob?.cancel()
    }

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "stopwatch_channel"
        private const val NOTIFICATION_ID = 111
        private const val ACTION_START_STOP_ID = 1
        private const val ACTION_LAP_ID = 0
        private const val ACTION_LAP = "lap"
        private const val ACTION_START = "start"
        private const val ACTION_STOP = "stop"
        private const val ACTION_RESET = "reset"
        const val ACTION_START_STOPWATCH_FOREGROUND = "start_stopwatch_foreground"
    }
}
