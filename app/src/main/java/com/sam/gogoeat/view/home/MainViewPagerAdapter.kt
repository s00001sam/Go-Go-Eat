package com.sam.gogoeat.view.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val fragments: ArrayList<Fragment>

    init {
        fragments = ArrayList()
    }

    fun addFragment(fragment: Fragment){
        fragments.add(fragment)
    }
    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}