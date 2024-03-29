package com.example.grocerystore.data.repository.dishes

import android.util.Log
import com.example.grocerystore.domain.models.item.DishUIState
import com.example.grocerystore.data.source.local.dishes.DishesLocalDataSource
import com.example.grocerystore.data.source.remove.retrofit.RetrofitDataSource
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
        val remoteItems = remoteSource.dishesAPI.getDishesList(dishListId)
        Log.d(TAG, "refreshDishesData : dishListId - $dishListId, : list = ${remoteItems?.items}")
        return if (remoteItems != null) {
            localSource.updateListDishes(remoteItems.items)
            Result.success(true)
        } else {
            Result.failure(NullPointerException("refreshDishesData : list = null"))
        }
    }



    override fun getDishesListFlow(): Flow<List<DishUIState>> = localSource.getListDishesFlow()

    override suspend fun getDishesList(): List<DishUIState>  = localSource.getListDishes()

    override fun getDishByIdFlow(dishId : String) : Flow<DishUIState?> = localSource.getDishByIdFlow(dishId)

    override suspend fun getDishById(dishId : String) : DishUIState? = localSource.getDishById(dishId)

}