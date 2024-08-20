package com.natife.timeforce.ui.screens.mainscreen.stopwatch.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.natife.timeforce.model.Lap
import com.natife.timeforce.ui.viewmodel.StopwatchViewModel
import kotlinx.coroutines.delay

@Composable
fun LapList(
    modifier: Modifier = Modifier,
    lapList: List<Lap>,
    fadeHeight: Dp,
) {
    val listState = rememberLazyListState()
    var readyList by remember { mutableStateOf(emptyList<Lap>()) }
    LaunchedEffect(key1 = lapList) {
        val list = lapList.toMutableList()
        if (list.isNotEmpty()) {
            list.add(Lap(0, 0, ""))
        }
        readyList = list
        delay(50)
        listState.animateScrollToItem(0)
    }
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        state = listState,
    ) {
        items(readyList, key = { it.id }) { lap ->
            if (lap.id == 0) {
                Spacer(modifier = Modifier.size(fadeHeight))
            } else {
                LapItem(
                    modifier = Modifier
                        .animateItem(),
                    lap = lap
                )
            }
        }
    }
}

@Composable
fun LapItem(modifier: Modifier = Modifier, lap: Lap) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(text = "${lap.lapCount}.")
        Spacer(modifier = Modifier.padding(8.dp))
        Text(text = lap.lap)
    }
}

@Composable
fun TimerView(modifier: Modifier = Modifier, viewModel: StopwatchViewModel) {
    val time by viewModel.mainTime.collectAsStateWithLifecycle()
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
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
                    text = time,
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}