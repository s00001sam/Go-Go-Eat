package com.sam.gogoeat.view.nearby

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.sam.gogoeat.databinding.FragmentNearbyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NearbyFragment : Fragment() {

    private val viewModel: NearbyViewModel by viewModels()
    private lateinit var binding: FragmentNearbyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNearbyBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}