package com.example.grocerystore.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.grocerystore.R
import com.example.grocerystore.databinding.ActivityMainBinding
import com.example.grocerystore.domain.adapters.PagerAdapter
import com.example.grocerystore.domain.helpers.Tab
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity()  {

    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }
    private fun init(){
        val listFragments = listOf(
            Tab(0,resources.getString(R.string.first_tab),ContextCompat.getDrawable(applicationContext,R.drawable.round_home_24),SectionsFragment.newInstance()),
            Tab(1,resources.getString(R.string.second_tab),ContextCompat.getDrawable(applicationContext,R.drawable.round_search_24),Fragment()),
            Tab(2,resources.getString(R.string.third_tab),ContextCompat.getDrawable(applicationContext,R.drawable.round_shopping_basket_24),BasketFragment.newInstance()),
            Tab(3,resources.getString(R.string.forth_tab),ContextCompat.getDrawable(applicationContext,R.drawable.outline_account_circle_24),Fragment()),
        )

        val adapter = PagerAdapter(this, listFragments)
        binding.viewPagerActivityMain.adapter = adapter
        binding.tabLayoutActivityMain.tabIconTint = null
        TabLayoutMediator(binding.tabLayoutActivityMain,binding.viewPagerActivityMain){
                tab,pos ->
            run {
                tab.text = listFragments[pos].name;
                tab.icon = listFragments[pos].icon
            }
        }.attach()
    }


}