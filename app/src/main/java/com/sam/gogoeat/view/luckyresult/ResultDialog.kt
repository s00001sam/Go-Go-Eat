package com.sam.gogoeat.view.luckyresult

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.sam.gogoeat.R
import com.sam.gogoeat.data.place.PlaceData
import com.sam.gogoeat.databinding.DialogResultBinding
import com.sam.gogoeat.utils.Util.gotoMap
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ResultDialog : AppCompatDialogFragment() {

    private lateinit var binding: DialogResultBinding
    private val viewModel: ResultViewModel by viewModels()

    companion object {
        private const val FRAGMENT_TAG = "ResultDialog"
        private const val NEW_PLACE_TAG = "NEW_PLACE_TAG"

        @JvmStatic
        fun newInstance(placeData: PlaceData): ResultDialog {
            val args = Bundle()
            val fragment = ResultDialog()
            args.putParcelable(NEW_PLACE_TAG, placeData)
            fragment.arguments = args
            return fragment
        }

        @JvmStatic
        fun show(
                fragmentManager: FragmentManager,
                placeData: PlaceData
        ): ResultDialog {
            val dialog = newInstance(placeData)
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DialogResultBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBundleData()
        initCollect()
        initView()
    }

    private fun initView() {
        binding.tvCancel.setOnClickListener {
            dismiss()
        }

        binding.tvGoMap.setOnClickListener {
            viewModel.newPlace.value.let { place ->
                activity?.gotoMap(place)
            }
            dismiss()
        }
    }

    fun getBundleData() {
        val newPlaceData = arguments?.getParcelable<PlaceData>(NEW_PLACE_TAG)
        newPlaceData?.let { viewModel.setNewPlace(newPlaceData) }
    }

    private fun initCollect() {
        lifecycleScope.launchWhenStarted {
            viewModel.newPlace.collectLatest {
                binding.tvName.text = it.name
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.leaveControl.collectLatest {
                if (it) {
                    dismiss()
                    viewModel.leaveComplete()
                }
            }
        }
    }
}