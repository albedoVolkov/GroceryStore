package com.example.grocerystore.domain.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.grocerystore.domain.helpers.Tab
import com.example.grocerystore.presentation.BasketFragment
import com.example.grocerystore.presentation.SectionsFragment

class PagerAdapter(fragmentActivity: FragmentActivity,private val list : List<Tab>): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return list[position].fragment
    }
}