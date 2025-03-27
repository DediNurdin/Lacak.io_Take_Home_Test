package com.dedinurdin.lacakiotakehometest.data.api

import com.dedinurdin.lacakiotakehometest.data.model.SensorData
import com.dedinurdin.lacakiotakehometest.data.model.VehicleLocation
import kotlin.random.Random

class MockFleetApi : FleetApiService {
    private val locations = listOf(
        VehicleLocation(-6.200000, 106.816666, System.currentTimeMillis()), // Jakarta
        VehicleLocation(-6.201000, 106.817666, System.currentTimeMillis()),
        VehicleLocation(-6.202000, 106.818666, System.currentTimeMillis()),
        VehicleLocation(-6.203000, 106.819666, System.currentTimeMillis()),
        VehicleLocation(-6.204000, 106.820666, System.currentTimeMillis())
    )

    private var currentLocationIndex = 0

    override suspend fun getCurrentLocation(): VehicleLocation {
        currentLocationIndex = (currentLocationIndex + 1) % locations.size
        return locations[currentLocationIndex].copy(timestamp = System.currentTimeMillis())
    }

    override suspend fun getSensorData(): SensorData {
        val random = Random.Default
        return SensorData(
            speed = random.nextInt(30, 100),
            isEngineOn = random.nextBoolean(),
            isDoorOpen = random.nextBoolean(),
            timestamp = System.currentTimeMillis()
        )
    }
}