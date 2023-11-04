package com.example.grocerystore.data.repository.dishes

import android.util.Log
import com.example.grocerystore.data.helpers.UIstates.item.DishUIState
import com.example.grocerystore.data.source.local.dishes.DishesLocalDataSource
import com.example.grocerystore.data.source.remove.retrofit.RetrofitDataSource
import com.example.grocerystore.ConstantsSource

class DishesRepository(
    private val dishesRemoteSource: RetrofitDataSource,
    private val dishesLocalSource: DishesLocalDataSource
    ) : DishesRepoInterface {

    private var pathEndId = -1

    companion object {
        private const val TAG = "DishesRepository"
    }


    override fun setDishListId(dishListId : Int) {
        pathEndId = dishListId
    }

    override suspend fun refreshDishesData(): Boolean {
        if(pathEndId == -1){
            throw Exception("id is not configured")
        }

        var path = ""
        when(pathEndId){
            111 -> {path = ConstantsSource.END_DISHES_1_URL_LINK}
            112 -> {path = ConstantsSource.END_DISHES_2_URL_LINK}
            113 -> {path = ConstantsSource.END_DISHES_3_URL_LINK}
            114 -> {path = ConstantsSource.END_DISHES_4_URL_LINK}
        }

        try {
            val remoteItems = dishesRemoteSource.dishesAPI.getDishesList(path)
            if (remoteItems != null) {
                Log.d(TAG, "refreshDishesData : list = ${remoteItems.items}")
                dishesLocalSource.deleteAllItems()
                dishesLocalSource.insertListOfItems(remoteItems.items)
                return true
            } else {
                Log.d(TAG, "refreshDishesData : data = null")
            }
        } catch (e: Exception) {
            Log.d(TAG, "refreshDishesData: Exception occurred, ${e.message}")
        }
        return false
    }

    override suspend fun observeDishItemById(dishId : Int) : DishUIState? {
        val res : DishUIState? = dishesLocalSource.getItemById(dishId)
        if(res != null){
            Log.d(TAG, "observeDishItemById : data : Success = $res")
        }else{
            Log.d(TAG, "observeDishItemById : data : Error = null")
        }
        return res
    }

    override suspend fun observeListDishes(): List<DishUIState> {
        val res : List<DishUIState> = dishesLocalSource.observeItems()
        Log.d(TAG, "observeListDishes : data = $res")
        return res
    }
}