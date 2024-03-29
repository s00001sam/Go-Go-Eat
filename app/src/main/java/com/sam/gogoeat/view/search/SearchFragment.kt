package com.sam.gogoeat.view.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.sam.gogoeat.BuildConfig
import com.sam.gogoeat.databinding.FragmentSearchBinding
import com.sam.gogoeat.utils.FAEvent
import com.sam.gogoeat.utils.FAParam
import com.sam.gogoeat.utils.UserManager
import com.sam.gogoeat.utils.Util.collectFlow
import com.sam.gogoeat.utils.Util.hideKeyboard
import com.sam.gogoeat.view.MainViewModel
import com.sam.gogoeat.view.support.BaseFragment
import com.sam.gogoeat.view.support.PriceSpinner
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : BaseFragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel : SearchViewModel by viewModels()
    private lateinit var mainViewModel: MainViewModel

    @Inject
    lateinit var priceSpinner: PriceSpinner

    @Inject
    lateinit var faTracker: FirebaseAnalytics

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        collectFlow()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        val debugStr = if (BuildConfig.DEBUG) " - debug" else ""
        binding.tvVersion.text = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})$debugStr"

        binding.sliderDistance.value = UserManager.mySettingData.distance.toFloat()
        binding.etWord.setOnFocusChangeListener { v, hasFocus ->
            viewModel.keyFocus.value = hasFocus
        }
        priceSpinner.setShowListener {
            viewModel.setPriceFocus(true)
        }
        priceSpinner.setDismissListener {
            viewModel.setPriceFocus(false)
        }
        priceSpinner.setItemClickListener { priceLevel, priceStr ->
            viewModel.setPrice(priceLevel, priceStr)
        }
        binding.btnPrice.setOnClickListener {
            it.doOnLayout { v ->
                priceSpinner.show(v, v.measuredWidth, viewModel.priceLevelNum)
            }
        }
        binding.ivBack.setOnClickListener {
            faTracker.logEvent(FAEvent.SEARCH_BACK) {}
            findNavController().navigateUp()
        }
        binding.root.setOnClickListener {
            it.hideKeyboard()
            binding.etWord.clearFocus()
        }
        binding.sliderDistance.addOnChangeListener { slider, value, fromUser ->
            viewModel.setDistance(value.toInt())
        }
        binding.btnReset.setOnClickListener {
            faTracker.logEvent(FAEvent.SEARCH_RESET) {}
            viewModel.resetData()
            binding.sliderDistance.value = 1000f
        }
        binding.btnSearch.setOnClickListener {
            faTracker.logEvent(FAEvent.SEARCH_GO) { param(FAParam.SEARCH_KEY, viewModel.keyWordStr.value) }
            viewModel.setData2UserManager()
            mainViewModel.getNearbyFoods(::dismissLoading)
            showLoading()
            findNavController().navigateUp()
        }
    }

    private fun collectFlow() {
        viewModel.distanceValue.collectFlow(viewLifecycleOwner) {
            binding.tvDistanceNumber.text = it.toString()
        }
    }
}