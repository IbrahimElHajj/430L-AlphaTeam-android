package com.iye03.currencyexchange

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabsPagerAdapterP2P(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                AllP2POptions()
            }
            1 -> {
                MyP2POptions()
            }
            else -> ExchangeFragment()
        }
    }
    override fun getItemCount(): Int {
        return 1 + if(Authentication.getToken() != null) 1 else 0;
    }
}
