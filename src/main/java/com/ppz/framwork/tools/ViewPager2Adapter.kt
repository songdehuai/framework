package com.ppz.framwork.tools

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlin.collections.ArrayList


class ViewPager2Adapter<T : Fragment>(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    val fragmentList = ArrayList<T>()
    private val fragmentTitleList = ArrayList<String>()

    fun clear() {
        fragmentList.clear()
        fragmentTitleList.clear()

    }

    fun addFragment(fragment: T, title: String = "") {
        fragmentList.add(fragment)
        fragmentTitleList.add(title)
    }

    fun getFragment(position: Int): T {
        return fragmentList[position]
    }

    fun getTitles(position: Int): String {
        return fragmentTitleList[position]
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }


    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

}