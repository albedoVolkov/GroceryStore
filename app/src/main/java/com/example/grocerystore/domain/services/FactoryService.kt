package com.example.grocerystore.domain.services

import android.util.Log
import androidx.fragment.app.Fragment
import com.example.grocerystore.domain.models.item.AddressUIState
import com.example.grocerystore.domain.models.item.CartUIState
import com.example.grocerystore.domain.models.item.DishUIState
import com.example.grocerystore.domain.models.item.OrderUIState
import com.example.grocerystore.domain.models.item.TabUIState
import com.example.grocerystore.domain.models.item.TitleUIState
import com.example.grocerystore.domain.models.user.UserUIState
import com.example.grocerystore.domain.utils.Utils

class FactoryService {

    companion object{
        const val TAG = "FactoryService"
    }

    private var idUser = 1_000_000
//    private var idAddress = 1_000_000
    private var idCart = 1_000_000_0
//    private var idCategory = 10_000
//    private var idDish = 1_000_000
    private var idTitle = 1_000_000
    private var idTab = 1_000


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
            idUser += 1
            return Result.success(
                UserUIState(
                    userId = idUser.toString(),
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
        }catch (e : Exception){
            Log.d(TAG,"createUserUIState - ERROR")
            return Result.failure(e)
        }
    }




//    suspend fun createDishUIState(
//        name:String,
//        image:String,
//        price:Int,
//        weight:Int,
//        description:String,
//        tags:List<String> = ArrayList(),
//    )
//    : Result<DishUIState?>{
//        try {
//            idDish += 1
//            return Result.success(
//                DishUIState(
//                    id = idDish.toString(),
//                    name,
//                    price,
//                    weight,
//                    description,
//                    image,
//                    tags,
//                )
//            )
//        }catch (e : Exception){
//            Log.d(TAG,"createDishUIState - ERROR")
//            return Result.failure(e)
//        }
//    }






    fun createTitleUIState(
        name:String,
        isSelected : Boolean = false
    )
    : Result<TitleUIState?>{
        return try {
            idTitle += 1
            Result.success(
                TitleUIState(
                    id = idTitle.toString(),
                    name,
                    isSelected,
                )
            )
        }catch (e : Exception){
            Log.d(TAG,"createTitleUIState - ERROR")
            Result.failure(e)
        }
    }





    fun createTabUIState(
        name:String,
        icon:Int,
        fragment:Fragment,
    )
    : Result<TabUIState?>{
        return try {
            idTab += 1
            Result.success(
                TabUIState(
                    id = idTab.toString(),
                    name,
                    icon,
                    fragment
                )
            )
        }catch (e : Exception){
            Log.d(TAG,"createTabUIState - ERROR")
            Result.failure(e)
        }
    }






//    suspend fun createCategoryUIState(
//        name:String,
//        image:String,
//    )
//    : Result<CategoryUIState?>{
//        return try {
//            idCategory += 1
//            Result.success(
//                CategoryUIState(
//                    id = idCategory.toString(),
//                    name,
//                    image,
//                )
//            )
//        }catch (e : Exception){
//            Log.d(TAG,"createCategoryUIState - ERROR")
//            Result.failure(e)
//        }
//    }






    fun createCartUIState(
        userId : String,
        itemData : DishUIState,
        quantity : Int
    )
    : Result<CartUIState?>{
        return try {
            idCart += 1
            Result.success(
                CartUIState(
                    cartId = idCart.toString(),
                    userId,
                    itemData,
                    quantity
                )
            )
        }catch (e : Exception){
            Log.d(TAG,"createCartUIState - ERROR")
            Result.failure(e)
        }
    }






//    suspend fun createAddressUIState(
//        fName: String = "",
//        lName: String = "",
//        countryISOCode: String = "",
//        streetAddress: String = "",
//        streetAddress2: String = "",
//        city: String = "",
//        state: String = "",
//        zipCode: String = "",
//        phoneNumber: String = ""
//    )
//    : Result<AddressUIState?>{
//        try {
//            idAddress += 1
//            return Result.success(
//                AddressUIState(
//                    idAddress.toString(),
//                    fName,
//                    lName,
//                    countryISOCode,
//                    streetAddress,
//                    streetAddress2,
//                    city,
//                    state,
//                    zipCode,
//                    phoneNumber,
//                )
//            )
//        }catch (e : Exception){
//            Log.d(TAG,"createAddressUIState - ERROR")
//            return Result.failure(e)
//        }
//    }


}