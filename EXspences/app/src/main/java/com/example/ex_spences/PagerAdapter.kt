package com.example.ex_spences

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            1 -> return AccountsFragment()
            2 -> return StatisticsFragment()
            3 -> return VaultFragment()
            else -> { // Note the block
                return MainFragment()
            }
        }
    }
}