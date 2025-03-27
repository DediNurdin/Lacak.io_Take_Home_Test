package com.dedinurdin.lacakiotakehometest.data.api

import com.dedinurdin.lacakiotakehometest.data.model.SensorData
import com.dedinurdin.lacakiotakehometest.data.model.VehicleLocation
import retrofit2.http.GET

interface FleetApiService {
    @GET("location")
    suspend fun getCurrentLocation(): VehicleLocation

    @GET("sensor-data")
    suspend fun getSensorData(): SensorData
}