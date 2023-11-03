package com.example.grocerystore.data.source.local.utilsData

import androidx.room.TypeConverter
import com.example.grocerystore.data.helpers.UIstates.item.AddressUIState
import com.example.grocerystore.data.helpers.UIstates.item.CartUIState
import com.example.grocerystore.data.helpers.UIstates.item.OrderUIState
import com.example.grocerystore.data.helpers.UIstates.item.fromStringToAddressItem
import com.example.grocerystore.data.helpers.UIstates.item.fromStringToCartItem
import com.example.grocerystore.data.helpers.UIstates.item.fromStringToOrderItem
import com.example.grocerystore.data.helpers.UIstates.user.UserUIState
import com.example.grocerystore.data.helpers.UIstates.user.fromStringToUserItem

class ListTypeConverter {
    @TypeConverter
    fun fromStringListToString(value: List<String>): String {
        return value.joinToString(separator = ",")
    }
    @TypeConverter
    fun fromStringToStringList(value: String): List<String> {
        if (value == ""){
            return listOf()
        }
        return value.split(",").map { it }
    }


    @TypeConverter
    fun fromUserUIStateListToString(value: List<UserUIState>): String {
        return value.joinToString(separator = ",")
    }
    @TypeConverter
    fun fromStringToUserUIStateList(value: String): List<UserUIState> {
        if (value == ""){
            return listOf()
        }
        return value.split(",").map { fromStringToUserItem(it)!! }
    }







    @TypeConverter
    fun fromAddressUIStateToString(value: AddressUIState): String {
        return value.toString()
    }
    @TypeConverter
    fun fromStringToAddressUIState(value: String): AddressUIState {
        return fromStringToAddressItem(value)!!
    }

    @TypeConverter
    fun fromAddressUIStateListToString(value: List<AddressUIState>): String {
        return value.joinToString(separator = ",")
    }
    @TypeConverter
    fun fromStringToAddressUIStateList(value: String): List<AddressUIState> {
        if (value == ""){
            return listOf()
        }
        return value.split(",").map { fromStringToAddressItem(it)!! }
    }







    @TypeConverter
    fun fromOrderUIStateToString(value: OrderUIState): String {
        return value.toString()
    }
    @TypeConverter
    fun fromStringToOrderUIState(value: String): OrderUIState {
        return fromStringToOrderItem(value)!!
    }

    @TypeConverter
    fun fromOrderUIStateListToString(value: List<OrderUIState>): String {
        return value.joinToString(separator = ",")
    }
    @TypeConverter
    fun fromStringToOrderUIStateList(value: String): List<OrderUIState> {
        if (value == ""){
            return listOf()
        }
        return value.split(",").map { fromStringToOrderItem(it)!! }
    }








    @TypeConverter
    fun fromCartUIStateToString(value: CartUIState): String {
        return value.toString()
    }
    @TypeConverter
    fun fromStringToCartUIState(value: String): CartUIState {
        return fromStringToCartItem(value)!!
    }

    @TypeConverter
    fun fromCartUIStateListToString(value: List<CartUIState>): String {
        return value.joinToString(separator = ",")
    }
    @TypeConverter
    fun fromStringToCartUIStateList(value: String): List<CartUIState> {
        if (value == ""){
            return listOf()
        }
        return value.split(",").map { fromStringToCartItem(it)!! }
    }





    @TypeConverter
    fun fromStringToIntList(value: String): List<Int> {
        if (value == ""){
            return listOf()
        }
        return value.split(",").map { it.toInt() }
    }
    @TypeConverter
    fun fromIntListToString(value: List<Int>): String {
        return value.joinToString(separator = ",")
    }

}