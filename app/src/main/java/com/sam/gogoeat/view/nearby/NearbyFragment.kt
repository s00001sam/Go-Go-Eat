package com.sam.gogoeat.view.nearby

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.sam.gogoeat.data.place.PlaceData.Companion.toGogoPlaces
import com.sam.gogoeat.databinding.FragmentNearbyBinding
import com.sam.gogoeat.utils.FAEvent
import com.sam.gogoeat.utils.Util.collectFlow
import com.sam.gogoeat.utils.Util.gotoMap
import com.sam.gogoeat.view.support.BaseFragment
import com.sam.gogoeat.view.MainViewModel
import com.sam.gogoeat.view.home.StoreAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NearbyFragment : BaseFragment() {

    private val viewModel: NearbyViewModel by viewModels()
    private lateinit var mainViewModel : MainViewModel
    private lateinit var binding: FragmentNearbyBinding

    @Inject
    lateinit var faTracker: FirebaseAnalytics

    private val storeAdapter : StoreAdapter by lazy {
        StoreAdapter(StoreAdapter.OnclickListener {
            faTracker.logEvent(FAEvent.NEARBY_GO_MAP) {}
            requireActivity().gotoMap(it)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNearbyBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeFlows()
    }

    private fun initView() {
        binding.rcyNearby.adapter = storeAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeFlows() {
        mainViewModel.nearbyFoodResult.collectFlow(viewLifecycleOwner) {
            if (it.isSuccess() && !it.data.isNullOrEmpty()) {
                val list = it.data.toGogoPlaces().sortedBy { it.distance }
                (binding.rcyNearby.adapter as StoreAdapter).submitList(list)
                (binding.rcyNearby.adapter as StoreAdapter).notifyDataSetChanged()
            }
        }
    }

}