package com.natife.timeforce.di

import com.natife.timeforce.ui.screens.mainscreen.stopwatch.Stopwatch
import com.natife.timeforce.ui.screens.mainscreen.stopwatch.StopwatchImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class StopwatchModule {

    @Binds
    abstract fun bindStopwatch(stopwatchImpl: StopwatchImpl): Stopwatch
}