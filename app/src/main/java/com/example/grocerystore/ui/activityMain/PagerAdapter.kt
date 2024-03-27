package com.example.grocerystore.ui.activityMain

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.grocerystore.domain.models.item.TabUIState

class PagerAdapter(fragmentActivity: FragmentActivity,private val list : List<TabUIState>): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return list[position].fragment
    }

}