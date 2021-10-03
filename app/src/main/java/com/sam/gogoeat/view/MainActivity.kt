package com.sam.gogoeat.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.sam.gogoeat.R
import com.sam.gogoeat.databinding.ActivityMainBinding
import com.sam.gogoeat.utils.UserManager
import com.sam.gogoeat.utils.Util.checkHasPermission
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        const val LOCATION_FINE = Manifest.permission.ACCESS_FINE_LOCATION
        const val LOCATION_COARSE = Manifest.permission.ACCESS_COARSE_LOCATION
    }

    val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    private val requestPermission by lazy {
        registerForActivityResult(ActivityResultContracts.RequestPermission()){ granted ->
            if (granted) {
                getNewLocation()
            } else {
                Toast.makeText(this, getString(R.string.no_location_permission), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            var lastLocation = p0.lastLocation
            UserManager.myLocation = LatLng(lastLocation.latitude, lastLocation.longitude)
            if (!viewModel.firstGetLocation) {
                viewModel.firstGetLocation = true
                viewModel.getNearbyFoods()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        checkLocationPermission()
    }

    fun checkLocationPermission() {
        if (!checkHasPermission(this, LOCATION_FINE)) {
            requestPermission.launch(LOCATION_FINE)
        } else {
            getNewLocation()
        }
    }

    private fun getNewLocation(){
        if (!checkHasPermission(this, LOCATION_FINE) && !checkHasPermission(this, LOCATION_COARSE)) {
            return
        }
        LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 10 * 1000
            fastestInterval = 10 * 1000
            fusedLocationProviderClient.requestLocationUpdates(this ,locationCallback, Looper.myLooper())
        }
    }

}