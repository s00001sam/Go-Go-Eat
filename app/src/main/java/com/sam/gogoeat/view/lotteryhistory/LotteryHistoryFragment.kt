package com.sam.gogoeat.view.lotteryhistory

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.sam.gogoeat.databinding.FragmentLotteryHistoryBinding
import com.sam.gogoeat.utils.Util.gotoMap
import com.sam.gogoeat.view.MainViewModel
import com.sam.gogoeat.view.home.StoreAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LotteryHistoryFragment : Fragment() {

    private val viewModel: LotteryHistoryViewModel by viewModels()
    private lateinit var binding: FragmentLotteryHistoryBinding
    private lateinit var mainViewModel: MainViewModel

    private val storeAdapter : StoreAdapter by lazy {
        StoreAdapter(StoreAdapter.OnclickListener {
            requireActivity().gotoMap(it)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLotteryHistoryBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        collectFlow()
    }

    private fun collectFlow() {

        lifecycleScope.launchWhenStarted {
            mainViewModel.historyList.collect {
                (binding.rcyHistory.adapter as StoreAdapter).submitList(it)
                (binding.rcyHistory.adapter as StoreAdapter).notifyDataSetChanged()
            }
        }
    }

    private fun initView() {
        binding.rcyHistory.adapter = storeAdapter
    }

}