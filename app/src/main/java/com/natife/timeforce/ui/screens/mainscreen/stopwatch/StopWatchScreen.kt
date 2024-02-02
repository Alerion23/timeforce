package com.natife.timeforce.ui.screens.mainscreen.stopwatch

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.natife.timeforce.R
import com.natife.timeforce.ui.theme.AppTheme
import com.natife.timeforce.utils.collectAsStateLifecycleAware

@Composable
fun StopWatchScreen(
    stopwatchViewModel: StopwatchViewModel = hiltViewModel()
) {
    AppTheme {
        Scaffold { innerPadding ->
            StopwatchView(
                innerPadding,
                stopwatchViewModel
            )
        }
    }
}

@Composable
fun StopwatchView(
    innerPaddingValues: PaddingValues,
    stopwatchViewModel: StopwatchViewModel
) {
    val context = LocalContext.current
    val localDensity = LocalDensity.current
    val mainTime: String by stopwatchViewModel.mainTime.collectAsStateLifecycleAware()
    val stopwatchState by stopwatchViewModel.stopwatchState.collectAsStateLifecycleAware()
    val cardWidth = remember {
        mutableStateOf(0.dp)
    }
    Column(
        modifier = Modifier
            .padding(innerPaddingValues)
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .onGloballyPositioned {
                    cardWidth.value = with(localDensity) {
                        it.size.width.toDp()
                    }
                }
        ) {
            Surface(
                modifier = Modifier
                    .size(cardWidth.value)
                    .padding(20.dp),
                shape = RoundedCornerShape(50),
                color = Color.Transparent,
                border = BorderStroke(10.dp, MaterialTheme.colorScheme.onPrimaryContainer)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = mainTime,
                        textAlign = TextAlign.Center,
                        fontSize = 40.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            StopwatchButtonsControl(
                stopwatchState = stopwatchState,
                stopwatchViewModel = stopwatchViewModel
            )
        }
    }
}

@Composable
fun ClearButtonStopwatch() {
    Button(onClick = {  }) {
        Icon(imageVector = Icons.Filled.Refresh, contentDescription = "Clear button")
    }
}

@Composable
fun StopwatchButtonsControl(
    stopwatchState: StopwatchState,
    stopwatchViewModel: StopwatchViewModel
) {
    val pauseIcon = ImageVector.vectorResource(id = R.drawable.icon_pause_24)
    val icon = remember {
        mutableStateOf(
            if (stopwatchState == StopwatchState.Stopped || stopwatchState == StopwatchState.Idle) {
                Icons.Filled.PlayArrow
            } else {
                pauseIcon
            }
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        if (stopwatchState == StopwatchState.Stopped) {
            ClearButtonStopwatch()
        }
        Button(
            modifier = Modifier
                .size(100.dp),
            onClick = {
                if (stopwatchState == StopwatchState.Started) {
                    icon.value = Icons.Filled.PlayArrow
                } else {
                    icon.value = pauseIcon
                }
                stopwatchViewModel.startStopwatch()
            }) {
            Icon(
                imageVector = icon.value, contentDescription = "Start/pause button"
            )
        }
        if (stopwatchState == StopwatchState.Started) {
            Button(onClick = {  }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.icon_stopwatch),
                    contentDescription = "Lap"
                )
            }
            Button(onClick = {}) {
                Icon(imageVector = Icons.Filled.Refresh, contentDescription = "Clear button")
            }
        }
    }
}