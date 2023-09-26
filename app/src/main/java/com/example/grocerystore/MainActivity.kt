package com.example.grocerystore


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.grocerystore.databinding.ActivityMainBinding
import com.example.grocerystore.ui.adapters.PagerAdapter
import com.example.grocerystore.data.helpers.TabUIState
import com.example.grocerystore.ui.BasketFragment
import com.example.grocerystore.ui.CategoriesFragment
import com.example.grocerystore.ui.InfoDishFragment
import com.example.grocerystore.ui.StoreFragment
import com.example.grocerystore.ui.viewModels.ActivityMainViewModel
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity()  {

    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!

    private var _viewModel: ActivityMainViewModel? = null
    private val viewModel get() = _viewModel!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        _viewModel = ActivityMainViewModel(GroceryStoreApplication(applicationContext))
        init()
    }

    private fun init(){
        val listFragments = listOf(
            TabUIState(
                0,
                resources.getString(R.string.first_tab),
                ContextCompat.getDrawable(applicationContext, R.drawable.round_home_24),
                CategoriesFragment.newInstance()
            ),
            TabUIState(
                1,
                resources.getString(R.string.second_tab)
                ,ContextCompat.getDrawable(applicationContext, R.drawable.round_search_24)
                ,Fragment()
            ),
            TabUIState(
                2,
                resources.getString(R.string.third_tab),
                ContextCompat.getDrawable(applicationContext, R.drawable.round_shopping_basket_24),
                BasketFragment.newInstance()
            ),
            TabUIState(
                3,
                resources.getString(R.string.forth_tab),
                ContextCompat.getDrawable(applicationContext, R.drawable.outline_account_circle_24),
                Fragment()
            ),
        )

        val adapter = PagerAdapter(this, listFragments)
        binding.viewPagerActivityMain.adapter = adapter
        TabLayoutMediator(binding.tabLayoutActivityMain,binding.viewPagerActivityMain){
                tab,pos ->
            run {
                tab.text = listFragments[pos].name
                tab.icon = listFragments[pos].icon
            }
        }.attach()
    }

}