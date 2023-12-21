package com.example.grocerystore.ui.activityMain


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.grocerystore.R
import com.example.grocerystore.data.helpers.UIstates.item.TabUIState
import com.example.grocerystore.databinding.ActivityMainBinding
import com.example.grocerystore.locateLazy
import com.example.grocerystore.services.CheckNetworkConnection
import com.example.grocerystore.ui.activityMain.fragments.firstTab.CategoriesFragment
import com.example.grocerystore.ui.activityMain.fragments.forthTab.AccountFragment
import com.example.grocerystore.ui.activityMain.fragments.thirdTab.BasketFragment
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity()  {

    private val TAG = "MainActivity"


    private lateinit var binding : ActivityMainBinding
    private val networkManager by locateLazy<CheckNetworkConnection>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showNoLoading(true)

        setView()
        //networkManager.observe(this){ showNoLoading(it == true) }
    }

    private fun <T> views(block : ActivityMainBinding.() -> T): T? = binding.block()

    private fun showNoLoading(success : Boolean) {
        views {
            if (!success) {
                loaderLayout.loaderBackground.visibility = View.VISIBLE
                containerMainActivityMain.visibility = View.GONE
                loaderLayout.circularLoader.showAnimationBehavior
            } else {
                loaderLayout.loaderBackground.visibility = View.GONE
                containerMainActivityMain.visibility = View.VISIBLE
            }
        }
    }


    private fun setView(){
        val listFragments = listOf(
            TabUIState(
                "0",
                resources.getString(R.string.first_tab),
                R.drawable.home_icon,
                CategoriesFragment.newInstance()
            ),
            TabUIState(
                "1",
                resources.getString(R.string.second_tab),
                R.drawable.search_icon,
                Fragment()
            ),
            TabUIState(
                "2",
                resources.getString(R.string.third_tab),
                R.drawable.basket_icon,
                BasketFragment.newInstance()
            ),
            TabUIState(
                "3",
                resources.getString(R.string.forth_tab),
                R.drawable.user_icon,
                AccountFragment.newInstance()
            ),
        )

        views {
            val adapter = PagerAdapter(this@MainActivity, listFragments)
            viewPagerActivityMain.adapter = adapter
            TabLayoutMediator(
                tabLayoutActivityMain,
                viewPagerActivityMain
            ) { tab, pos ->
                run {
                    tab.text = listFragments[pos].name
                    tab.icon =
                        ContextCompat.getDrawable(applicationContext, listFragments[pos].icon)
                }
            }.attach()
        }
    }

}