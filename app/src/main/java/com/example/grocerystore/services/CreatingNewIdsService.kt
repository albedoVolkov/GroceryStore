package com.example.grocerystore.services

import android.util.Log

class CreatingNewIdsService {

    companion object{
        const val TAG = "CreatingNewIdsService"
    }

    private var id_user = 1_000_000
    private var id_address = 1_000_000
    private var id_cart = 1_000_000
    private var id_category = 1_000_000
    private var id_dish = 1_000_000
    private var id_title = 1_000_000
    private var id_tab = 1_000_000

    suspend fun createIdForUserUIState() : Result<String?>{
        try {
            //creating id in fireBase
            return Result.success((id_user+1).toString())
        }catch (e : Exception){
            Log.d(TAG,"createIdForUserUIState - ERROR")
            return Result.failure(e)
        }
    }

    suspend fun createIdForCategoryUIState() : Result<String?>{
        try {
            //creating id in fireBase
            val newId = id_category+1
            Log.d(TAG,"createIdForCategoryUIState - $newId -SUCCESS")
            return Result.success((newId).toString())
        }catch (e : Exception){
            Log.d(TAG,"createIdForCategoryUIState - ERROR")
            return Result.failure(e)
        }
    }

    suspend fun createIdForDishUIState() : Result<String?>{
        try {
            //creating id in fireBase
            val newId = id_dish+1
            Log.d(TAG,"createIdForDishUIState - $newId -SUCCESS")
            return Result.success((newId).toString())
        }catch (e : Exception){
            Log.d(TAG,"createIdForDishUIState - ERROR")
            return Result.failure(e)
        }
    }


    suspend fun createIdForTitleUIState() : Result<String?>{
        try {
            //creating id in fireBase
            val newId = id_title+1
            Log.d(TAG,"createIdForTitleUIState - $newId -SUCCESS")
            return Result.success((newId).toString())
        }catch (e : Exception){
            Log.d(TAG,"createIdForTitleUIState - ERROR")
            return Result.failure(e)
        }
    }


    suspend fun createIdForTabUIState() : Result<String?>{
        try {
            //creating id in fireBase
            val newId = id_tab+1
            Log.d(TAG,"createIdForTabUIState - $newId -SUCCESS")
            return Result.success((newId).toString())
        }catch (e : Exception){
            Log.d(TAG,"createIdForTabUIState - ERROR")
            return Result.failure(e)
        }
    }

    suspend fun createIdForCartUIState() : Result<String?>{
        try {
            //creating id in fireBase
            val newId = id_cart+1
            Log.d(TAG,"createIdForCartUIState - $newId -SUCCESS")
            return Result.success((newId).toString())
        }catch (e : Exception){
            Log.d(TAG,"createIdForCartUIState - ERROR")
            return Result.failure(e)
        }
    }

    suspend fun createIdForAddressUIState() : Result<String?>{
        try {
            //creating id in fireBase
            val newId = id_address+1
            Log.d(TAG,"createIdForAddressUIState - $newId -SUCCESS")
            return Result.success((newId).toString())
        }catch (e : Exception){
            Log.d(TAG,"createIdForAddressUIState - ERROR")
            return Result.failure(e)
        }
    }


}