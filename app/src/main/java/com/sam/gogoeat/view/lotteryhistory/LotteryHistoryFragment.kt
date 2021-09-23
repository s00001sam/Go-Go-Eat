package com.sam.gogoeat.view.lotteryhistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.sam.gogoeat.databinding.FragmentLotteryHistoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LotteryHistoryFragment : Fragment() {

    private val viewModel: LotteryHistoryViewModel by viewModels()
    private lateinit var binding: FragmentLotteryHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLotteryHistoryBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}