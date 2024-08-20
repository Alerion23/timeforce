package com.natife.timeforce

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.natife.timeforce.data.service.StopwatchService
import com.natife.timeforce.ui.navigation.bottom.MainNavigationGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TimeForceActivity : ComponentActivity() {

    private val notificationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
        callback = { isGranted ->
            if (!isGranted) {
                Toast.makeText(
                    this,
                    getString(R.string.notification_permission_is_required_for_better_experience),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainNavigationGraph()
        }
        checkNotificationPermission(this@TimeForceActivity) {
            notificationPermission.launch(it)
        }
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, StopwatchService::class.java)
        stopService(intent)
    }

    override fun onStop() {
        super.onStop()
        val intent = Intent(this, StopwatchService::class.java).also {
            it.action = StopwatchService.ACTION_START_STOPWATCH_FOREGROUND
        }
        startService(intent)
    }

    private fun checkNotificationPermission(
        context: Context, isNotGranted: (String) -> Unit
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            val granted = PackageManager.PERMISSION_GRANTED
            if (ContextCompat.checkSelfPermission(context, permission) != granted) {
                isNotGranted(permission)
            }
        }
    }

    override fun onDestroy() {
        stopService(Intent(this, StopwatchService::class.java))
        super.onDestroy()
    }
}