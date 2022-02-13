package com.sam.gogoeat.view.lotteryhistory

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.sam.gogoeat.databinding.FragmentLotteryHistoryBinding
import com.sam.gogoeat.utils.Util.collectFlow
import com.sam.gogoeat.utils.Util.gotoMap
import com.sam.gogoeat.view.support.BaseFragment
import com.sam.gogoeat.view.MainViewModel
import com.sam.gogoeat.view.home.StoreAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LotteryHistoryFragment : BaseFragment() {

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

    @SuppressLint("NotifyDataSetChanged")
    private fun collectFlow() {
        mainViewModel.insertHistoryResult.collectFlow(viewLifecycleOwner) {
            if (!it.isNothing()) {
                viewModel.getHistories()
                mainViewModel.completeInsertHistories()
            }
        }

        viewModel.getHistoriesResult.collectFlow(viewLifecycleOwner) {
            if (it.isSuccess()) {
                it.data?.reversed()?.let {
                    (binding.rcyHistory.adapter as StoreAdapter).submitList(it)
                    (binding.rcyHistory.adapter as StoreAdapter).notifyDataSetChanged()
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.rcyHistory.smoothScrollToPosition(0)
                    }, 500)
                }
            }
        }
    }

    private fun initView() {
        binding.rcyHistory.adapter = storeAdapter
    }

}