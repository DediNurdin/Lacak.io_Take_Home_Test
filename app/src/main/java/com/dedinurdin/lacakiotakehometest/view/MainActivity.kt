package com.dedinurdin.lacakiotakehometest.view

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.dedinurdin.lacakiotakehometest.R
import com.dedinurdin.lacakiotakehometest.data.api.MockFleetApi
import com.dedinurdin.lacakiotakehometest.data.model.SensorData
import com.dedinurdin.lacakiotakehometest.data.model.VehicleLocation
import com.dedinurdin.lacakiotakehometest.data.repository.FleetRepository
import com.dedinurdin.lacakiotakehometest.databinding.ActivityMainBinding
import com.dedinurdin.lacakiotakehometest.viewmodel.FleetViewModel
import com.dedinurdin.lacakiotakehometest.viewmodel.FleetViewModelFactory
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: FleetViewModel
    private lateinit var map: MapView
    private lateinit var mapController: IMapController
    private var vehicleMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = FleetRepository(MockFleetApi())
        viewModel = ViewModelProvider(
            this,
            FleetViewModelFactory(repository)
        )[FleetViewModel::class.java]

        setupMap()
        setupObservers()
    }

    private fun setupMap() {
        Configuration.getInstance().load(
            applicationContext,
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )

        map = binding.mapView
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)

        mapController = map.controller
        mapController.setZoom(15.0)

        val startPoint = GeoPoint(-6.200000, 106.816666)
        vehicleMarker = Marker(map).apply {
            position = startPoint
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            icon = ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_vehicle)
        }
        map.overlays.add(vehicleMarker)
        mapController.setCenter(startPoint)
    }

    private fun setupObservers() {
        viewModel.vehicleLocation.observe(this) { location ->
            updateVehicleLocation(location)
        }

        viewModel.sensorData.observe(this) { sensorData ->
            updateDashboard(sensorData)
        }

        viewModel.alerts.observe(this) { alert ->
            Toast.makeText(this, alert, Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateVehicleLocation(location: VehicleLocation) {
        val geoPoint = GeoPoint(location.latitude, location.longitude)

        vehicleMarker?.position = geoPoint
        mapController.animateTo(geoPoint)

        binding.locationInfo.text =
            "Lat: ${"%.4f".format(location.latitude)}\n" + "Lng: ${"%.4f".format(location.longitude)}"

        map.invalidate()
    }

    @SuppressLint("SetTextI18n")
    private fun updateDashboard(sensorData: SensorData) {
        binding.speedValue.text = sensorData.speed.toString()
        binding.speedProgressBar.progress = sensorData.speed

        binding.engineStatus.text = if (sensorData.isEngineOn) "ON" else "OFF"
        binding.engineStatus.setTextColor(
            ContextCompat.getColor(
                this,
                if (sensorData.isEngineOn) android.R.color.holo_green_dark else android.R.color.holo_red_dark
            )
        )

        binding.doorStatus.text = if (sensorData.isDoorOpen) "OPEN" else "CLOSED"
        binding.doorStatus.setTextColor(
            ContextCompat.getColor(
                this,
                if (sensorData.isDoorOpen) android.R.color.holo_red_dark else android.R.color.holo_green_dark
            )
        )

        updateProgressBarColor(sensorData.speed)
    }

    private fun updateProgressBarColor(speed: Int) {
        val progressDrawable = binding.speedProgressBar.progressDrawable.mutate()
        val color = when {
            speed > 100 -> ContextCompat.getColor(this, android.R.color.holo_red_dark)
            speed > 80 -> ContextCompat.getColor(this, android.R.color.holo_orange_dark)
            else -> ContextCompat.getColor(this, android.R.color.holo_blue_dark)
        }
        progressDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        binding.speedProgressBar.progressDrawable = progressDrawable
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }
}