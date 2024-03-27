package com.example.grocerystore.ui.activityMain

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerystore.R
import com.example.grocerystore.domain.models.item.TabUIState
import com.example.grocerystore.locateLazy
import com.example.grocerystore.domain.services.CheckNetworkConnection
import com.example.grocerystore.domain.services.FactoryService
import com.example.grocerystore.ui.activityMain.fragments.firstTab.CategoriesFragment
import com.example.grocerystore.ui.activityMain.fragments.forthTab.AccountFragment
import com.example.grocerystore.ui.activityMain.fragments.thirdTab.BasketFragment
import kotlinx.coroutines.launch

class MainViewModel  : ViewModel() {


    companion object {
        const val TAG = "MainViewModel"
    }


    val networkManager by locateLazy<CheckNetworkConnection>()
    private val factory by locateLazy<FactoryService>()



    var mainTabs: List<TabUIState> = emptyList() // this list isn't for showing and not sorted




    fun refreshData(listStrings : List<String> ) : Boolean{

        var listTabs : List<TabUIState> = listOf()

        viewModelScope.launch {
            listTabs = listOf(
                factory.createTabUIState(
                    listStrings[0],
                    R.drawable.home_icon,
                    CategoriesFragment.newInstance()
                ).getOrNull()!!,
                factory.createTabUIState(
                    listStrings[1],
                    R.drawable.search_icon,
                    Fragment()
                ).getOrNull()!!,
                factory.createTabUIState(
                    listStrings[2],
                    R.drawable.basket_icon,
                    BasketFragment.newInstance()
                ).getOrNull()!!,
                factory.createTabUIState(
                    listStrings[3],
                    R.drawable.user_icon,
                    AccountFragment.newInstance()
                ).getOrNull()!!,
            )
        }
        mainTabs = listTabs
        return true
    }

}