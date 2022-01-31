package com.sam.gogoeat.view

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.sam.gogoeat.R
import com.sam.gogoeat.databinding.ActivityMainBinding
import com.sam.gogoeat.utils.UserManager
import com.sam.gogoeat.utils.Util.checkHasPermission
import com.sam.gogoeat.utils.Util.startShakeAnim
import com.sam.gogoeat.view.loading.LoadingDialog
import com.sam.gogoeat.view.search.SearchDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        const val LOCATION_FINE = Manifest.permission.ACCESS_FINE_LOCATION
        const val LOCATION_COARSE = Manifest.permission.ACCESS_COARSE_LOCATION
    }

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private var dialog: AppCompatDialogFragment? = null
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
            val lastLocation = p0.lastLocation
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
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        checkLocationPermission()
        initView()
    }

    private fun initView() {
        binding.ivSearch.setOnClickListener {
            it.startShakeAnim(0.5f, 1.5f, 30f, 1000) {
                SearchDialog.show(supportFragmentManager)
            }
        }
    }

    private fun checkLocationPermission() {
        if (!checkHasPermission(LOCATION_FINE)) {
            requestPermission.launch(LOCATION_FINE)
        } else {
            getNewLocation()
        }
    }

    private fun getNewLocation(){
        if (!checkHasPermission(LOCATION_FINE) && !checkHasPermission(LOCATION_COARSE)) {
            return
        }
        LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 10 * 1000
            fastestInterval = 10 * 1000
            Looper.myLooper()?.let {
                fusedLocationProviderClient.requestLocationUpdates(this ,locationCallback, it)
            }
        }
    }

    fun showLoading() {
        if (dialog == null) {
            dialog = LoadingDialog.show(supportFragmentManager)
        }
    }

    fun dismissLoading() {
        dialog?.dismiss()
        dialog = null
    }

}