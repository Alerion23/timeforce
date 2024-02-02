package com.natife.timeforce

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.natife.timeforce.navigation.MainNavigationGraph
import com.natife.timeforce.service.StopwatchService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainNavigationGraph()
        }
    }

    override fun onDestroy() {
        stopService(Intent(this, StopwatchService::class.java))
        super.onDestroy()
    }
}