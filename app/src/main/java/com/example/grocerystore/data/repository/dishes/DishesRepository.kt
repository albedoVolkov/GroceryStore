package com.example.grocerystore.data.repository.dishes

import android.util.Log
import com.example.grocerystore.data.helpers.UIstates.item.DishUIState
import com.example.grocerystore.data.source.local.dishes.DishesLocalDataSource
import com.example.grocerystore.data.source.remove.retrofit.RetrofitDataSource
import com.example.grocerystore.services.ConstantsSource
import kotlinx.coroutines.flow.Flow

class DishesRepository(
    private val remoteSource: RetrofitDataSource,
    private val localSource: DishesLocalDataSource
    ) : DishesRepoInterface {

    private val TAG = "DishesRepository"




    override suspend fun refreshDishesData(dishListId : String): Boolean {

            if(dishListId == "-1"){
                throw Exception("id is not configured")
            }

            var path = ""
            when(dishListId){
                "111" -> {path = ConstantsSource.END_DISHES_1_URL_LINK}
                "112" -> {path = ConstantsSource.END_DISHES_2_URL_LINK}
                "113" -> {path = ConstantsSource.END_DISHES_3_URL_LINK}
                "114" -> {path = ConstantsSource.END_DISHES_4_URL_LINK}
            }



            val remoteItems = remoteSource.dishesAPI.getDishesList(path)
            if (remoteItems != null) {
                Log.d(TAG, "refreshDishesData : list = ${remoteItems.items}")
                localSource.updateListDishes(remoteItems.items)
                return true
            } else {
                Log.d(TAG, "refreshDishesData : data = null")
            }
        return false
    }



    override fun getDishesListFlow(): Flow<List<DishUIState>> = localSource.getListDishesFlow()

    override suspend fun getDishesList(): List<DishUIState>  = localSource.getListDishes()

    override fun getDishByIdFlow(dishId : String) : Flow<DishUIState?> = localSource.getDishByIdFlow(dishId)

    override suspend fun getDishById(dishId : String) : DishUIState? = localSource.getDishById(dishId)

}