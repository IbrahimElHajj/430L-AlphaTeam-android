package com.iye03.currencyexchange

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabsPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                ExchangeFragment()
            }
            1 -> {
                PriceTraceFragment()
            }
            2 -> {
                TransactionsFragment()
            }
            else -> ExchangeFragment()
        }
    }
    override fun getItemCount(): Int {
        return 2 + if(Authentication.getToken() != null) 1 else 0;
    }
}
