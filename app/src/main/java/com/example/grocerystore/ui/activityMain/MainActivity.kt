package com.example.grocerystore.ui.activityMain


import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.grocerystore.R
import com.example.grocerystore.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity()  {

    private val TAG = "MainActivity"


    private lateinit var binding : ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showNoLoading(true)
        setView()

        viewModel.networkManager.observe(this){ showNoLoading(it == true) }
    }



    private fun <T> views(block : ActivityMainBinding.() -> T): T? = binding.block()



    private fun setView(){

        val listStrings = listOf(
            resources.getString(R.string.first_tab),
            resources.getString(R.string.second_tab),
            resources.getString(R.string.third_tab),
            resources.getString(R.string.forth_tab),
        )

        viewModel.refreshData(listStrings)

        views {
            val adapter = PagerAdapter(this@MainActivity, viewModel.mainTabs)
            viewPagerActivityMain.adapter = adapter
            TabLayoutMediator(
                tabLayoutActivityMain,
                viewPagerActivityMain
            ) { tab, pos ->
                run {
                    tab.text = viewModel.mainTabs[pos].name
                    tab.icon =
                        ContextCompat.getDrawable(this@MainActivity, viewModel.mainTabs[pos].icon)
                }
            }.attach()
        }
    }


    fun changeFragment(item : Int) {
        views {
            viewPagerActivityMain.setCurrentItem(item,true)
        }
    }


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


}