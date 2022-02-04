package com.sam.gogoeat.view

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.*
import com.sam.gogoeat.R
import com.sam.gogoeat.databinding.ActivityMainBinding
import com.sam.gogoeat.utils.Logger
import com.sam.gogoeat.utils.UserManager
import com.sam.gogoeat.utils.Util.checkHasPermission
import com.sam.gogoeat.utils.Util.collectFlow
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
    private val locationClient: FusedLocationProviderClient by lazy {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        checkLocationPermission()
        initView()
        initCollect()
    }

    private fun initView() {
        binding.ivSearch.setOnClickListener {
            it.startShakeAnim(0.5f, 1.5f, 30f, 1000) {
                SearchDialog.show(supportFragmentManager)
            }
        }
    }

    private fun initCollect() {
        viewModel.locationResult.collectFlow(this) {
            Logger.d("my location=${it.data}")
            if (it.isSuccess() && it.data != null) {
                UserManager.myLocation = it.data
                if (!viewModel.firstGetLocation) {
                    viewModel.firstGetLocation = true
                    viewModel.getNearbyFoods()
                }
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
        viewModel.getLocation(locationClient)
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