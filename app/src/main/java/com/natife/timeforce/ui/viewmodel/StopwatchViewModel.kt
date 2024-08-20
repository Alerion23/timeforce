package com.natife.timeforce.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natife.timeforce.data.manager.StopwatchManager
import com.natife.timeforce.data.manager.StopwatchManagerImpl
import com.natife.timeforce.model.Lap
import com.natife.timeforce.ui.screens.mainscreen.stopwatch.StopwatchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StopwatchViewModel @Inject constructor(
    private val stopwatchManager: StopwatchManager
) : ViewModel() {

    private val _mainTime = MutableStateFlow(StopwatchManagerImpl.DEFAULT_STOPWATCH_TIME)
    val mainTime = _mainTime

    private val _stopwatchState = MutableStateFlow(StopwatchState.Idle)
    val stopwatchState = _stopwatchState

    private val _lapList = MutableStateFlow(emptyList<Lap>())
    val lapList = _lapList

    fun startObserving() {
        viewModelScope.launch {
            stopwatchManager.getTime().collectLatest {
                _mainTime.value = it
            }
        }
        viewModelScope.launch {
            stopwatchManager.getStopwatchState().collectLatest {
                _stopwatchState.value = it
            }
        }
        viewModelScope.launch {
            stopwatchManager.getLapList().collectLatest {
                _lapList.value = it
            }
        }
    }

    fun startStopwatch() {
        viewModelScope.launch {
            stopwatchManager.start()
        }
    }

    fun stop() {
        viewModelScope.launch {
            stopwatchManager.stop()
        }
    }

    fun reset() {
        viewModelScope.launch {
            stopwatchManager.reset()
        }
    }

    fun lap() {
        viewModelScope.launch {
            stopwatchManager.lap()
        }
    }
}