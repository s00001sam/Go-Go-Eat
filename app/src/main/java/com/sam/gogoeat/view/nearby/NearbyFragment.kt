package com.sam.gogoeat.view.nearby

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.sam.gogoeat.databinding.FragmentNearbyBinding
import com.sam.gogoeat.utils.Util.gotoMap
import com.sam.gogoeat.view.MainViewModel
import com.sam.gogoeat.view.home.StoreAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class NearbyFragment : Fragment() {

    private val viewModel: NearbyViewModel by viewModels()
    private lateinit var mainViewModel : MainViewModel
    private lateinit var binding: FragmentNearbyBinding

    private val storeAdapter : StoreAdapter by lazy {
        StoreAdapter(StoreAdapter.OnclickListener {
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

    private fun observeFlows() {
        lifecycleScope.launchWhenStarted {
            mainViewModel.nearbyFoodResult.collect {
                if (it.isSuccess() && !it.data.isNullOrEmpty()) {
                    (binding.rcyNearby.adapter as StoreAdapter).submitList(it.data.sortedBy { it.distance })
                    (binding.rcyNearby.adapter as StoreAdapter).notifyDataSetChanged()
                }
            }
        }
    }

}