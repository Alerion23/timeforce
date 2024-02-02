package com.natife.timeforce.ui.screens.mainscreen.stopwatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StopwatchViewModel @Inject constructor(
    private val stopwatch: Stopwatch
) : ViewModel() {

    private val _mainTime = MutableStateFlow("00:00:00:00")
    val mainTime: StateFlow<String> = _mainTime

    private val _stopwatchState = MutableStateFlow(StopwatchState.Idle)
    val stopwatchState: StateFlow<StopwatchState> = _stopwatchState

    init {
        viewModelScope.launch(Dispatchers.Main) {
            stopwatch.getTime().collectLatest {
                _mainTime.value = it
            }
        }
        viewModelScope.launch(Dispatchers.Main) {
            stopwatch.getStopwatchState().collectLatest {
                _stopwatchState.value = it
            }
        }
    }

    fun startStopwatch() {
        stopwatch.execute()
    }
}