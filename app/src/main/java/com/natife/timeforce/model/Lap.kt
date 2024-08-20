package com.natife.timeforce.model

import androidx.compose.runtime.Stable

@Stable
data class Lap(
    val id: Int,
    val lapCount: Int,
    val lap: String
)
