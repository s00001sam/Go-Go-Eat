package com.sam.gogoeat.view.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.sam.gogoeat.R
import com.sam.gogoeat.data.place.PlaceData.Companion.toGogoPlaces
import com.sam.gogoeat.databinding.FragmentHomeBinding
import com.sam.gogoeat.utils.FAEvent
import com.sam.gogoeat.utils.UserManager
import com.sam.gogoeat.utils.Util.collectFlow
import com.sam.gogoeat.utils.Util.gotoMap
import com.sam.gogoeat.view.MainViewModel
import com.sam.gogoeat.view.lotteryhistory.LotteryHistoryFragment
import com.sam.gogoeat.view.luckyresult.ResultDialog
import com.sam.gogoeat.view.nearby.NearbyFragment
import com.sam.gogoeat.view.support.BaseFragment
import com.sam.gogoeat.view.support.PressBackHelper
import com.sam.gogoeat.view.support.PriceLevel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.reflect.Field
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var bottomBehavior: BottomSheetBehavior<View>

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    private val collapseStoreAdapter : CollapseStoreAdapter by lazy {
        CollapseStoreAdapter(CollapseStoreAdapter.OnclickListener {
            faTracker.logEvent(FAEvent.HOME_BOTTOM_GO_MAP) {}
            requireActivity().gotoMap(it)
        })
    }

    @Inject
    lateinit var pressBackHelper: PressBackHelper

    @Inject
    lateinit var faTracker: FirebaseAnalytics

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (bottomBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                collapseBottomSheet()
            } else {
                if (!findNavController().popBackStack()) {
                    pressBackHelper.back(requireActivity())
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.luckyWheelView.visibility = View.VISIBLE
        binding.luckyWheelView.showWithAnimation()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

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
        collectFlows()
    }

    private fun initViews() {
        addTags()
        initRcyCollapse()
        setTabAndViewPager()
        setBottomSheet()

        binding.luckyWheelView.setOnClickListener {
            faTracker.logEvent(FAEvent.HOME_CLICK_WHEEL) {}
            binding.luckyWheelView.setList(mainViewModel.getNearByGogoPlaces())
            binding.luckyWheelView.setRandomIndex(mainViewModel.getRandomIndex())
            binding.luckyWheelView.startScroll()
        }
        binding.luckyWheelView.setScrollFinishListener {
            mainViewModel.getRandomFoodIntoHistory()
        }
        binding.bsAllList.tvSeeMore.setOnClickListener {
            faTracker.logEvent(FAEvent.HOME_SEE_MORE) {}
            showBottomSheet()
        }
        binding.ivSearch.setOnClickListener {
            faTracker.logEvent(FAEvent.HOME_CLICK_SEARCH) {}
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment())
        }
    }

    private fun addTags() {
        UserManager.mySettingData.run {
            binding.chipTags.removeAllViews()
            if (!keyWord.isNullOrEmpty()) setChip(keyWord!!)
            setChip(getString(R.string.meter_radius, distance))
            if (priceLevel != PriceLevel.NONE.ordinal) setChip(UserManager.getMyPriceStr())
            if (isOpen) setChip(getString(R.string.is_open))
            setChip(if (onlyFindRestaurant) getString(R.string.is_restaurant) else getString(R.string.is_not_restaurant))
        }
    }

    private fun setChip(tag: String) {
        val chip = Chip(requireContext())
        chip.text = tag
        chip.setChipBackgroundColorResource(R.color.orange_top)
        chip.setTextAppearance(R.style.ChipTextAppearance)
        binding.chipTags.addView(chip)
    }

    private fun initRcyCollapse() {
        binding.bsAllList.rvCollapseStore.adapter = collapseStoreAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun collectFlows() {
        mainViewModel.newHistoryItem.collectFlow(viewLifecycleOwner) {
            it?.let {
                ResultDialog.show(parentFragmentManager, it)
                mainViewModel.newHistoryShowFinish()
            }
        }

        mainViewModel.nearbyFoodResult.collectFlow(viewLifecycleOwner) {
            if (it.isLoading()) showLoading() else dismissLoading()
            if (it.isSuccess() && !it.data.isNullOrEmpty()) {
                val list = it.data.toGogoPlaces().sortedBy { it.distance }
                (binding.bsAllList.rvCollapseStore.adapter as CollapseStoreAdapter).submitList(list)
                (binding.bsAllList.rvCollapseStore.adapter as CollapseStoreAdapter).notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setBottomSheet() {
        bottomBehavior = BottomSheetBehavior.from(binding.bsAllList.root)
        bottomBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset != 0f) {
                    tabLayout.visibility = View.VISIBLE
                    viewPager.visibility = View.VISIBLE
                }
                tabLayout.alpha = slideOffset
                viewPager.alpha = slideOffset
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        tabLayout.visibility = View.GONE
                        viewPager.visibility = View.GONE
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {

                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        tabLayout.isVisible = true
                        viewPager.isVisible = true
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {

                    }
                    BottomSheetBehavior.STATE_SETTLING -> {

                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        TODO()
                    }
                }
            }
        })
    }

    fun showBottomSheet() {
        bottomBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    fun collapseBottomSheet() {
        bottomBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun switchViewpager(position: Int) {
        binding.bsAllList.mainTablayout.setScrollPosition(position, 0f, true)
        binding.bsAllList.mainViewpager.setCurrentItem(position, true)
    }

    private fun setTabAndViewPager() {
        tabLayout = binding.bsAllList.mainTablayout
        viewPager = binding.bsAllList.mainViewpager
        tabLayout.alpha = 0f
        val viewPagerAdapter = MainViewPagerAdapter(this)
        viewPagerAdapter.addFragment(NearbyFragment())
        viewPagerAdapter.addFragment(LotteryHistoryFragment())
        viewPager.apply {
            adapter = viewPagerAdapter
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
        improveViewpagerSensitivity()
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Nearby"
                else -> "History"
            }
        }.attach()
    }

    private fun improveViewpagerSensitivity() {
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