package com.dedinurdin.lacakiotakehometest.data.model

data class SensorData(
    val speed: Int,
    val isEngineOn: Boolean,
    val isDoorOpen: Boolean,
    val timestamp: Long
)