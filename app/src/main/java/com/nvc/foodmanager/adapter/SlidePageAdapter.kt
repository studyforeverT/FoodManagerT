package com.nvc.foodmanager.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nvc.foodmanager.fragment.*
import java.lang.IllegalArgumentException

class SlidePageAdapter(activity : AppCompatActivity)
    : FragmentStateAdapter(activity) {
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> MainFragment()
            1 -> MainFoodFragment()
            2 -> MainOrderFragment()
            else -> throw IllegalArgumentException("Unknown for position $position")
        }
    }


}