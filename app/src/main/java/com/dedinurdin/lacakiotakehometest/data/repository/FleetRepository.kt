package com.dedinurdin.lacakiotakehometest.data.repository

import com.dedinurdin.lacakiotakehometest.data.api.FleetApiService
import com.dedinurdin.lacakiotakehometest.data.model.SensorData
import com.dedinurdin.lacakiotakehometest.data.model.VehicleLocation

class FleetRepository(private val apiService: FleetApiService) {
    suspend fun getVehicleLocation(): VehicleLocation = apiService.getCurrentLocation()
    suspend fun getSensorData(): SensorData = apiService.getSensorData()
}