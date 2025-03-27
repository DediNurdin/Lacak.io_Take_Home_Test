package com.dedinurdin.lacakiotakehometest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dedinurdin.lacakiotakehometest.data.model.SensorData
import com.dedinurdin.lacakiotakehometest.data.model.VehicleLocation
import com.dedinurdin.lacakiotakehometest.data.repository.FleetRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class FleetViewModel(private val repository: FleetRepository) : ViewModel() {
    private val _vehicleLocation = MutableLiveData<VehicleLocation>()
    val vehicleLocation: LiveData<VehicleLocation> = _vehicleLocation

    private val _sensorData = MutableLiveData<SensorData>()
    val sensorData: LiveData<SensorData> = _sensorData

    private val _alerts = MutableLiveData<String>()
    val alerts: LiveData<String> = _alerts

    private val locationUpdateJob = Job()
    private val scope = CoroutineScope(Dispatchers.IO + locationUpdateJob)

    init {
        startSimulation()
    }

    private fun startSimulation() {
        scope.launch {
            while (true) {
                delay(3000) // Update every 3 seconds
                fetchData()
            }
        }
    }

    private suspend fun fetchData() {
        try {
            val location = repository.getVehicleLocation()
            val sensor = repository.getSensorData()

            _vehicleLocation.postValue(location)
            _sensorData.postValue(sensor)

            if (sensor.speed > 80) {
                _alerts.postValue("Speed alert: ${sensor.speed} km/h")
            }
            if (sensor.isDoorOpen && sensor.speed > 0) {
                _alerts.postValue("Door open while moving!")
            }
            if (_sensorData.value?.isEngineOn != sensor.isEngineOn) {
                _alerts.postValue("Engine ${if (sensor.isEngineOn) "started" else "stopped"}")
            }
        } catch (e: Exception) {
            Timber.e(e, "Error fetching data")
        }
    }

    override fun onCleared() {
        super.onCleared()
        locationUpdateJob.cancel()
    }
}