package com.sam.gogoeat.view.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.sam.gogoeat.databinding.FragmentStartBinding
import com.sam.gogoeat.view.support.BaseFragment

class StartFragment : BaseFragment() {

    private lateinit var binding: FragmentStartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vLogo.setAnimationEndListener {
            lifecycleScope.launchWhenResumed {
                findNavController().navigate(StartFragmentDirections.actionStartFragmentToHomeFragment())
            }
        }
    }
}