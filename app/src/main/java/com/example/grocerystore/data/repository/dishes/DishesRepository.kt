package com.example.grocerystore.data.repository.dishes

import android.util.Log
import com.example.grocerystore.domain.models.item.DishUIState
import com.example.grocerystore.data.source.local.dishes.DishesLocalDataSource
import com.example.grocerystore.data.source.remove.retrofit.RetrofitDataSource
import com.example.grocerystore.data.utils.ConstantsData
import kotlinx.coroutines.flow.Flow

class DishesRepository(
    private val remoteSource: RetrofitDataSource,
    private val localSource: DishesLocalDataSource
    ) : DishesRepoInterface {

    companion object{
        const val TAG = "DishesRepository"
    }

    override suspend fun refreshDishesData(dishListId : String): Result<Boolean?> {
        Log.d(TAG, "refreshDishesData : dishListId - $dishListId")
        if (dishListId == "111" || dishListId == "112" || dishListId == "113" || dishListId == "114" ) {

                var path = ""
                when (dishListId) {
                    "111" -> {
                        path = ConstantsData.END_DISHES_1_URL_LINK
                    }

                    "112" -> {
                        path = ConstantsData.END_DISHES_2_URL_LINK
                    }

                    "113" -> {
                        path = ConstantsData.END_DISHES_3_URL_LINK
                    }

                    "114" -> {
                        path = ConstantsData.END_DISHES_4_URL_LINK
                    }
                }
            Log.d(TAG, "refreshDishesData : path - $path")


            val remoteItems = remoteSource.dishesAPI.getDishesList(path)
            Log.d(TAG, "refreshDishesData : list = ${remoteItems?.items}")
            return if (remoteItems != null) {
                localSource.updateListDishes(remoteItems.items)
                Result.success(true)
            } else {
                Result.success(false)
            }
            }else{
            Log.d(TAG, "refreshDishesData : dishListId != 111|112|113|114")
            return Result.success(false)
        }
    }



    override fun getDishesListFlow(): Flow<List<DishUIState>> = localSource.getListDishesFlow()

    override suspend fun getDishesList(): List<DishUIState>  = localSource.getListDishes()

    override fun getDishByIdFlow(dishId : String) : Flow<DishUIState?> = localSource.getDishByIdFlow(dishId)

    override suspend fun getDishById(dishId : String) : DishUIState? = localSource.getDishById(dishId)

}