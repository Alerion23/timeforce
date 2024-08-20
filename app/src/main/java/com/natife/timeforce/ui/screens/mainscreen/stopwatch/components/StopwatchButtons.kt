package com.natife.timeforce.ui.screens.mainscreen.stopwatch.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Autorenew
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.natife.timeforce.R
import com.natife.timeforce.ui.screens.mainscreen.stopwatch.StopwatchState

@Composable
fun StopwatchButtonsControl(
    modifier: Modifier = Modifier,
    state: StopwatchState,
    fadeHeight: (Dp) -> Unit,
    onMainClick: (StopwatchState) -> Unit,
    onResetClick: () -> Unit,
    onLapClick: () -> Unit
) {
    val background = MaterialTheme.colorScheme.background
    val localDensity = LocalDensity.current
    Column(modifier = modifier
        .drawBehind {
            drawRect(Brush.verticalGradient(listOf(Color.Transparent, background, background)))
        }
        .onSizeChanged {
            val height = with(localDensity) { it.height.toDp() }
            fadeHeight(height)
        }, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            AnimatedVisibility(visible = state == StopwatchState.Started || state == StopwatchState.Stopped) {
                SecondaryStopwatchButton(
                    icon = Icons.Rounded.Autorenew,
                    onClick = onResetClick
                )
            }
            MainStopwatchButton(state = state, onClick = onMainClick)
            AnimatedVisibility(visible = state == StopwatchState.Started) {
                SecondaryStopwatchButton(icon = Icons.Rounded.Timer, onClick = onLapClick)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }

}

@Composable
fun SecondaryStopwatchButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    onClick: () -> Unit
) {
    val buttonColor = MaterialTheme.colorScheme.primaryContainer

    Box(modifier = modifier
        .padding(8.dp)
        .clip(CircleShape)
        .drawBehind {
            drawCircle(
                color = buttonColor, radius = 24.dp.toPx()
            )
        }
        .clickable {
            onClick()
        }, contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon, contentDescription =
            stringResource(R.string.secondary_button), modifier = Modifier
                .padding(16.dp)
                .size(35.dp),
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun MainStopwatchButton(
    modifier: Modifier = Modifier, state: StopwatchState, onClick: (StopwatchState) -> Unit
) {
    val configuration = LocalConfiguration.current
    val mainButtonSize = remember {
        configuration.screenWidthDp / 10
    }
    val colorAnimation: Color by animateColorAsState(
        targetValue = if (state == StopwatchState.Started) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
        label = stringResource(R.string.color),
        animationSpec = tween(500)
    )

    val shapeAnimation by animateDpAsState(
        targetValue = if (state == StopwatchState.Started) 17.dp else 50.dp,
        label = stringResource(R.string.shape),
        animationSpec = spring(stiffness = 1000f, dampingRatio = 0.5f)
    )
    Box(modifier = modifier
        .padding(8.dp)
        .clip(RoundedCornerShape(shapeAnimation))
        .drawBehind {
            drawRoundRect(
                color = colorAnimation, cornerRadius = CornerRadius(16.dp.toPx())
            )
        }
        .clickable {
            onClick(if (state == StopwatchState.Started) StopwatchState.Stopped else StopwatchState.Started)
        }, contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = if (state == StopwatchState.Started) R.drawable.icon_pause_24 else R.drawable.icon_play),
            contentDescription = stringResource(R.string.start_stop_button),
            modifier = Modifier
                .padding(mainButtonSize.dp/2)
                .size(mainButtonSize.dp),
            tint = Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ButtonsPreview() {
    var state by remember { mutableStateOf(StopwatchState.Idle) }
    StopwatchButtonsControl(
        state = state,
        onMainClick = { state = it },
        onResetClick = { },
        onLapClick = { },
        fadeHeight = { })
}