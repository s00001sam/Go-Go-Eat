package com.sam.gogoeat.view.search

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.sam.gogoeat.R
import com.sam.gogoeat.databinding.DialogSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchDialog : AppCompatDialogFragment() {

    private lateinit var binding: DialogSearchBinding
    private val viewModel : SearchViewModel by viewModels()

    companion object {
        private const val FRAGMENT_TAG = "SearchDialog"

        @JvmStatic
        fun newInstance(): SearchDialog {
            val args = Bundle()
            val fragment = SearchDialog()
            fragment.arguments = args
            return fragment
        }

        @JvmStatic
        fun show(
            fragmentManager: FragmentManager
        ): SearchDialog {
            val dialog = newInstance()
            dialog.show(fragmentManager, FRAGMENT_TAG)
            return dialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogStyle)
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        dialog?.window?.let {
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogSearchBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.llSearchKey.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_from_right))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.ivBack.setOnClickListener { dismiss() }
    }
}