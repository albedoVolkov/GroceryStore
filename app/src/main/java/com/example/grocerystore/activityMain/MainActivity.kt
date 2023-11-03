package com.example.grocerystore.activityMain


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.grocerystore.GroceryStoreApplication
import com.example.grocerystore.R
import com.example.grocerystore.activityMain.mainViewModel.ActivityMainViewModel
import com.example.grocerystore.activityMain.mainViewModel.ActivityMainViewModelFactory
import com.example.grocerystore.data.helpers.UIstates.item.TabUIState
import com.example.grocerystore.databinding.ActivityMainBinding
import com.example.grocerystore.activityMain.ui.firstTab.CategoriesFragment
import com.example.grocerystore.activityMain.ui.forthTab.AccountFragment
import com.example.grocerystore.activityMain.ui.thirdTab.BasketFragment
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity()  {

    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!

    private var _viewModel: ActivityMainViewModel? = null
    private var _viewModelFactory: ActivityMainViewModelFactory? = null
    private val viewModel get() = _viewModel!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        _viewModelFactory = ActivityMainViewModelFactory(GroceryStoreApplication(applicationContext))
        _viewModel = ViewModelProvider(this, _viewModelFactory!!)[ActivityMainViewModel::class.java]
        setView()
        setObservers()
    }

    private fun setView(){

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
                AccountFragment()
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

    private fun setObservers(){


    }
    //Bundle bundle = new Bundle();
    //bundle.putString("edttext", "From Activity");
    //// set Fragmentclass Arguments
    //Fragmentclass fragobj = new Fragmentclass();
    //fragobj.setArguments(bundle);


}