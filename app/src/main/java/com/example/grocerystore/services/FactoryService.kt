package com.example.grocerystore.services

import android.util.Log
import androidx.fragment.app.Fragment
import com.example.grocerystore.data.helpers.UIstates.item.AddressUIState
import com.example.grocerystore.data.helpers.UIstates.item.CartUIState
import com.example.grocerystore.data.helpers.UIstates.item.CategoryUIState
import com.example.grocerystore.data.helpers.UIstates.item.DishUIState
import com.example.grocerystore.data.helpers.UIstates.item.OrderUIState
import com.example.grocerystore.data.helpers.UIstates.item.TabUIState
import com.example.grocerystore.data.helpers.UIstates.item.TitleUIState
import com.example.grocerystore.data.helpers.UIstates.user.UserUIState
import com.example.grocerystore.data.helpers.Utils

class FactoryService {

    companion object{
        const val TAG = "FactoryService"
    }

    private var id_user = 1_000_000
    private var id_address = 1_000_000
    private var id_cart = 1_000_000_0
    private var id_category = 10_000
    private var id_dish = 1_000_000
    private var id_title = 1_000_000
    private var id_tab = 1_000





    private suspend fun createIdForUserUIState() : Result<String?>{
        try {
            //creating id in fireBase
            val newId = id_user
            id_user++
            Log.d(TAG,"createIdForUserUIState - $newId - SUCCESS")
            return Result.success(newId.toString())
        }catch (e : Exception){
            Log.d(TAG,"createIdForUserUIState - ERROR")
            return Result.failure(e)
        }
    }
    private suspend fun createIdForCategoryUIState() : Result<String?>{
        try {
            //creating id in fireBase
            val newId = id_category
            id_category++
            Log.d(TAG,"createIdForCategoryUIState - $newId - SUCCESS")
            return Result.success((newId).toString())
        }catch (e : Exception){
            Log.d(TAG,"createIdForCategoryUIState - ERROR")
            return Result.failure(e)
        }
    }
    private suspend fun createIdForDishUIState() : Result<String?>{
        try {
            //creating id in fireBase
            val newId = id_dish
            id_dish++
            Log.d(TAG,"createIdForDishUIState - $newId - SUCCESS")
            return Result.success((newId).toString())
        }catch (e : Exception){
            Log.d(TAG,"createIdForDishUIState - ERROR")
            return Result.failure(e)
        }
    }
    private suspend fun createIdForTitleUIState() : Result<String?>{
        try {
            //creating id in fireBase
            val newId = id_title
            id_title++
            Log.d(TAG,"createIdForTitleUIState - $newId - SUCCESS")
            return Result.success((newId).toString())
        }catch (e : Exception){
            Log.d(TAG,"createIdForTitleUIState - ERROR")
            return Result.failure(e)
        }
    }
    private suspend fun createIdForTabUIState() : Result<String?>{
        try {
            //creating id in fireBase
            val newId = id_tab
            id_tab++
            Log.d(TAG,"createIdForTabUIState - $newId - SUCCESS")
            return Result.success((newId).toString())
        }catch (e : Exception){
            Log.d(TAG,"createIdForTabUIState - ERROR")
            return Result.failure(e)
        }
    }
    private suspend fun createIdForCartUIState() : Result<String?>{
        try {
            //creating id in fireBase
            val newId = id_cart
            id_cart++
            Log.d(TAG,"createIdForCartUIState - $newId - SUCCESS")
            return Result.success((newId).toString())
        }catch (e : Exception){
            Log.d(TAG,"createIdForCartUIState - ERROR")
            return Result.failure(e)
        }
    }
    private suspend fun createIdForAddressUIState() : Result<String?>{
        try {
            //creating id in fireBase
            val newId = id_address
            id_address++
            Log.d(TAG,"createIdForAddressUIState - $newId - SUCCESS")
            return Result.success((newId).toString())
        }catch (e : Exception){
            Log.d(TAG,"createIdForAddressUIState - ERROR")
            return Result.failure(e)
        }
    }










    suspend fun createUserUIState(
        name:String,
        image:String,
        phone:String,
        email:String,
        password:String,
        likes:List<String> = ArrayList(),
        address:List<AddressUIState> = ArrayList(),
        cart:List<CartUIState> = ArrayList(),
        orders:List<OrderUIState> = ArrayList(),
        userType:String = Utils.UserType.CUSTOMER.name,
        )
    : Result<UserUIState?>{
        try {
            val id = createIdForUserUIState()
            if(id.isSuccess && id.getOrNull() != null) {
                Log.d(TAG, "createUserUIState - $id - SUCCESS")
                return Result.success(
                    UserUIState(
                    id.getOrNull()!!,
                        name,
                        image,
                        phone,
                        email,
                        password,
                        likes,
                        address,
                        cart,
                        orders,
                        userType
                    )
                )
            }
            throw Exception("newId is fail")
        }catch (e : Exception){
            Log.d(TAG,"createUserUIState - ERROR")
            return Result.failure(e)
        }
    }




    suspend fun createDishUIState(
        name:String,
        image:String,
        price:Int,
        weight:Int,
        description:String,
        tags:List<String> = ArrayList(),
    )
    : Result<DishUIState?>{
        try {
            val id = createIdForDishUIState()
            if(id.isSuccess && id.getOrNull() != null) {
                Log.d(TAG, "createDishUIState - $id - SUCCESS")
                return Result.success(
                    DishUIState(
                        id.getOrNull()!!,
                        name,
                        price,
                        weight,
                        description,
                        image,
                        tags,
                    )
                )
            }
            throw Exception("newId is fail")
        }catch (e : Exception){
            Log.d(TAG,"createDishUIState - ERROR")
            return Result.failure(e)
        }
    }






    suspend fun createTitleUIState(
        name:String,
        isSelected : Boolean = false
    )
    : Result<TitleUIState?>{
        try {
            val id = createIdForTitleUIState()
            if(id.isSuccess && id.getOrNull() != null) {
                Log.d(TAG, "createTitleUIState - $id - SUCCESS")
                return Result.success(
                    TitleUIState(
                        id.getOrNull()!!,
                        name,
                        isSelected,
                    )
                )
            }
            throw Exception("newId is fail")
        }catch (e : Exception){
            Log.d(TAG,"createTitleUIState - ERROR")
            return Result.failure(e)
        }
    }





    suspend fun createTabUIState(
        name:String,
        icon:Int,
        fragment:Fragment,
    )
    : Result<TabUIState?>{
        try {
            val id = createIdForTabUIState()
            if(id.isSuccess && id.getOrNull() != null) {
                Log.d(TAG, "createTabUIState - $id - SUCCESS")
                return Result.success(
                    TabUIState(
                        id.getOrNull()!!,
                        name,
                        icon,
                        fragment
                    )
                )
            }
            throw Exception("newId is fail")
        }catch (e : Exception){
            Log.d(TAG,"createTabUIState - ERROR")
            return Result.failure(e)
        }
    }






    suspend fun createCategoryUIState(
        name:String,
        image:String,
    )
    : Result<CategoryUIState?>{
        try {
            val id = createIdForCategoryUIState()
            if(id.isSuccess && id.getOrNull() != null) {
                Log.d(TAG, "createCategoryUIState - $id - SUCCESS")
                return Result.success(
                    CategoryUIState(
                        id.getOrNull()!!,
                        name,
                        image,
                    )
                )
            }
            throw Exception("newId is fail")
        }catch (e : Exception){
            Log.d(TAG,"createCategoryUIState - ERROR")
            return Result.failure(e)
        }
    }






    suspend fun createCartUIState(
        userId : String,
        itemData : DishUIState,
        quantity : Int
    )
    : Result<CartUIState?>{
        try {
            val id = createIdForCartUIState()
            if(id.isSuccess && id.getOrNull() != null) {
                Log.d(TAG, "createCartUIState - $id - SUCCESS")
                return Result.success(
                    CartUIState(
                        cartId = id.getOrNull()!!,
                        userId,
                        itemData,
                        quantity
                    )
                )
            }
            throw Exception("newId is fail")
        }catch (e : Exception){
            Log.d(TAG,"createCartUIState - ERROR")
            return Result.failure(e)
        }
    }






    suspend fun createAddressUIState(
        fName: String = "",
        lName: String = "",
        countryISOCode: String = "",
        streetAddress: String = "",
        streetAddress2: String = "",
        city: String = "",
        state: String = "",
        zipCode: String = "",
        phoneNumber: String = ""
    )
    : Result<AddressUIState?>{
        try {
            val id = createIdForAddressUIState()
            if(id.isSuccess && id.getOrNull() != null) {
                Log.d(TAG, "createAddressUIState - $id - SUCCESS")
                return Result.success(
                    AddressUIState(
                        id.getOrNull()!!,
                        fName,
                        lName,
                        countryISOCode,
                        streetAddress,
                        streetAddress2,
                        city,
                        state,
                        zipCode,
                        phoneNumber,
                    )
                )
            }
            throw Exception("newId is fail")
        }catch (e : Exception){
            Log.d(TAG,"createAddressUIState - ERROR")
            return Result.failure(e)
        }
    }


}