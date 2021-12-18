package com.sam.gogoeat.view.home

import android.animation.Animator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sam.gogoeat.data.place.PlaceData
import com.sam.gogoeat.databinding.FragmentHomeBinding
import com.sam.gogoeat.view.MainViewModel
import com.sam.gogoeat.view.lotteryhistory.LotteryHistoryFragment
import com.sam.gogoeat.view.luckyresult.ResultDialog
import com.sam.gogoeat.view.nearby.NearbyFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import java.lang.reflect.Field

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var bottomBehavior: BottomSheetBehavior<View>

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeFlows()
    }

    private fun initViews() {
        setBottomSheet()
        setTabAndViewPager()

        binding.lavWheel.addAnimatorListener(object : Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                mainViewModel.getRandomFoodIntoHistory()
            }

            override fun onAnimationCancel(animation: Animator?) {}

            override fun onAnimationStart(animation: Animator?) {}
        })

        binding.vClick.setOnClickListener {
            binding.lavWheel.playAnimation()
        }
    }

    private fun observeFlows() {
        lifecycleScope.launchWhenStarted {
            mainViewModel.listClick.collect { listNeedOpen ->
                if (listNeedOpen) {
                    showBottomSheet()
                } else {
                    hideBottomSheet()
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            mainViewModel.newHistoryItem.collectLatest {
                it?.let {
                    ResultDialog.show(parentFragmentManager, it)
                }
            }
        }
    }

    private fun setBottomSheet() {
        bottomBehavior = BottomSheetBehavior.from(bs_all_list)
        bottomBehavior.isDraggable = false
        bottomBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {

                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {

                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {

                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {

                    }
                    BottomSheetBehavior.STATE_SETTLING -> {

                    }
                }
            }
        })
    }

    fun showBottomSheet() {
        bottomBehavior.isHideable = false
        bottomBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    fun hideBottomSheet() {
        bottomBehavior.isHideable = true
        bottomBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun switchViewpager(position: Int) {
        binding.bsAllList.mainTablayout.setScrollPosition(position, 0f, true)
        binding.bsAllList.mainViewpager.setCurrentItem(position, true)
    }

    private fun setTabAndViewPager() {
        val tabLayout: TabLayout = binding.bsAllList.mainTablayout
        val viewPager: ViewPager2 = binding.bsAllList.mainViewpager
        val viewPagerAdapter = MainViewPagerAdapter(this)
        viewPagerAdapter.addFragment(NearbyFragment())
        viewPagerAdapter.addFragment(LotteryHistoryFragment())
        viewPager.apply {
            adapter = viewPagerAdapter
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
        downViewpager2Sensitivity()
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Nearby"
                else -> "History"
            }
        }.attach()
    }

    fun downViewpager2Sensitivity() {
        try {
            val recyclerViewField: Field = ViewPager2::class.java.getDeclaredField("mRecyclerView")
            recyclerViewField.setAccessible(true)
            val recyclerView = recyclerViewField.get(binding.bsAllList.mainViewpager) as RecyclerView
            val touchSlopField: Field = RecyclerView::class.java.getDeclaredField("mTouchSlop")
            touchSlopField.setAccessible(true)
            val touchSlop = touchSlopField.get(recyclerView) as Int
            touchSlopField.set(recyclerView, touchSlop * 5)
        } catch (e: Exception) {
            Log.d("sam","sam00 降低 Viewpager2 靈敏度錯誤=${e.message}")
        }
    }

}