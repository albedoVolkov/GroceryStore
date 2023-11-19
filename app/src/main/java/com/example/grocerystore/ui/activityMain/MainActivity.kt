package com.example.grocerystore.ui.activityMain


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.grocerystore.GroceryStoreApplication
import com.example.grocerystore.R
import com.example.grocerystore.ui.activityMain.mainViewModel.ActivityMainViewModel
import com.example.grocerystore.ui.activityMain.mainViewModel.ActivityMainViewModelFactory
import com.example.grocerystore.data.helpers.UIstates.item.TabUIState
import com.example.grocerystore.data.helpers.UIstates.user.UserUIState
import com.example.grocerystore.databinding.ActivityMainBinding
import com.example.grocerystore.ui.activityMain.fragments.firstTab.CategoriesFragment
import com.example.grocerystore.ui.activityMain.fragments.forthTab.AccountFragment
import com.example.grocerystore.ui.activityMain.fragments.thirdTab.BasketFragment
import com.example.grocerystore.ui.activityMain.fragments.thirdTab.factories.BasketFragmentViewModelFactory
import com.example.grocerystore.ui.activityMain.fragments.thirdTab.viewModels.BasketFragmentViewModel
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity()  {


    companion object{
        const val TAG = "MainActivity"
    }


    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!

    private var _viewModel: ActivityMainViewModel? = null
    private val viewModel get() = _viewModel!!
    private var _viewModelFactory: ActivityMainViewModelFactory? = null

    private var _user : UserUIState? = null
    private val user get() = _user!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        _viewModelFactory = ActivityMainViewModelFactory(GroceryStoreApplication(this).userRepository)
        _viewModel = ViewModelProvider(this, _viewModelFactory!!)[ActivityMainViewModel::class.java]
        setView()
        setObservers()
    }



    private fun setView(){
        GroceryStoreApplication(applicationContext).sessionManager.updateDataLoginSession(isBlackMode = false, isLogin = true, isRememberMe = true)


        val listFragments = listOf(
            TabUIState(
                "0",
                resources.getString(R.string.first_tab),
                ContextCompat.getDrawable(applicationContext, R.drawable.round_home_24),
                CategoriesFragment.newInstance()
            ),
            TabUIState(
                "1",
                resources.getString(R.string.second_tab)
                ,ContextCompat.getDrawable(applicationContext, R.drawable.round_search_24)
                ,Fragment()
            ),
            TabUIState(
                "2",
                resources.getString(R.string.third_tab),
                ContextCompat.getDrawable(applicationContext, R.drawable.round_shopping_basket_24),
                BasketFragment.newInstance()
            ),
            TabUIState(
                "3",
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



    private fun setObservers() {

        viewModel.userData.observe(this) {
            if (it != null) {
                Log.d(TAG, "userData = $it")
                _user = it
            }else{
                Log.d(TAG, "userData is null")
            }
        }


    }






}