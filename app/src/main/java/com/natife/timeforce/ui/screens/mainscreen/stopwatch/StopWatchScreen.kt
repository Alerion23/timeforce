package com.natife.timeforce.ui.screens.mainscreen.stopwatch

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.natife.timeforce.R
import com.natife.timeforce.ui.screens.mainscreen.stopwatch.components.LapList
import com.natife.timeforce.ui.screens.mainscreen.stopwatch.components.StopwatchButtonsControl
import com.natife.timeforce.ui.screens.mainscreen.stopwatch.components.TimerView
import com.natife.timeforce.ui.theme.TimeForceTheme
import com.natife.timeforce.ui.viewmodel.StopwatchViewModel

@Composable
fun StopWatchScreen(
    viewModel: StopwatchViewModel = hiltViewModel()
) {
    TimeForceTheme {
        StopwatchView(
            viewModel
        )
    }
}

@OptIn(ExperimentalMotionApi::class)
@Composable
fun StopwatchView(
    viewModel: StopwatchViewModel
) {

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val context = LocalContext.current
        val sceneFile = R.raw.scene_stopwatch
        val motionSceneContent = remember(sceneFile) {
            context.resources
                .openRawResource(sceneFile)
                .readBytes()
                .decodeToString()
        }
        var fadeHeight by remember {
            mutableStateOf(0.dp)
        }
        val (buttons, timer) = createRefs()
        val currentState by viewModel.stopwatchState.collectAsStateWithLifecycle()
        val lapList by viewModel.lapList.collectAsStateWithLifecycle()
        val progress by animateFloatAsState(
            targetValue = if (lapList.isNotEmpty()) 1f else 0f,
            animationSpec = tween(1000), label = "",
        )
        val buttonHeightAnimation by animateDpAsState(
            targetValue = if (lapList.isNotEmpty()) 0.dp else fadeHeight,
            label = "", animationSpec = tween(1000)
        )
        LaunchedEffect(key1 = Unit) {
            viewModel.startObserving()
        }
        MotionLayout(
            modifier = Modifier
                .constrainAs(timer) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                .padding(bottom = buttonHeightAnimation),
            motionScene = MotionScene(content = motionSceneContent),
            progress = progress
        ) {
            val properties = motionProperties(id = "timer_view")
            Spacer(modifier = Modifier.layoutId("middle_line"))
            LapList(
                lapList = lapList,
                modifier = Modifier.layoutId("laps_list"),
                fadeHeight = fadeHeight,
            )
            TimerView(
                viewModel = viewModel,
                modifier = Modifier
                    .layoutId("timer_view")
                    .aspectRatio(properties.value.float("ratio")),
            )
        }
        StopwatchButtonsControl(
            modifier = Modifier.constrainAs(buttons) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            },
            state = currentState,
            onMainClick = { state ->
                when (state) {
                    StopwatchState.Started -> {
                        viewModel.startStopwatch()
                    }

                    StopwatchState.Stopped -> {
                        viewModel.stop()
                    }

                    StopwatchState.Idle -> {
                        viewModel.startStopwatch()
                    }
                }
            },
            onResetClick = {
                viewModel.reset()
            },
            onLapClick = {
                viewModel.lap()
            },
            fadeHeight = {
                fadeHeight = it
            })
    }
}