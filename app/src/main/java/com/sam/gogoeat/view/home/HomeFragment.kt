package com.sam.gogoeat.view.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sam.gogoeat.databinding.FragmentHomeBinding
import com.sam.gogoeat.view.lotteryhistory.LotteryHistoryFragment
import com.sam.gogoeat.view.nearby.NearbyFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.lang.reflect.Field

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getNearbyFoods()
        observeFlows()
        setTabAndViewPager()
    }
    
    private fun observeFlows() {
        lifecycleScope.launchWhenStarted {
            viewModel.nearbyFoodResult.collect {
                Log.d("sam", "sam00 foods result=${it}")
            }
        }
    }

    private fun setTabAndViewPager() {
        val tabLayout: TabLayout = binding.mainTablayout
        val viewPager: ViewPager2 = binding.mainViewpager
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
            val recyclerView = recyclerViewField.get(binding.mainViewpager) as RecyclerView
            val touchSlopField: Field = RecyclerView::class.java.getDeclaredField("mTouchSlop")
            touchSlopField.setAccessible(true)
            val touchSlop = touchSlopField.get(recyclerView) as Int
            touchSlopField.set(recyclerView, touchSlop * 5)
        } catch (e: Exception) {
            Log.d("sam","sam00 降低 Viewpager2 靈敏度錯誤=${e.message}")
        }
    }

}