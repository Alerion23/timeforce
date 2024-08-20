package com.natife.timeforce.di

import com.natife.timeforce.data.manager.StopwatchManager
import com.natife.timeforce.data.manager.StopwatchManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class StopwatchBindModule {

    @Binds
    abstract fun bindStopwatch(stopwatchImpl: StopwatchManagerImpl): StopwatchManager
}