package com.example.grocerystore.data.helpers.UIstates.item

import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson

@Entity(tableName = "cartsList")
data class CartUIState(
    @PrimaryKey
    @ColumnInfo(name = "cartId")
    var cartId : String,
    @ColumnInfo(name = "userId")
    var userId : String,
    @ColumnInfo(name = "itemData")
    var itemData: DishUIState,
    @ColumnInfo(name = "quantity")
    var quantity: Int = 1,
){
    override fun toString(): String {
        return Gson().toJson(this,CartUIState::class.java)
    }
}


fun fromStringToCartItem(cartString: String): CartUIState? {
    try {
        Log.d("CartUIState", " fromStringToCartItem : cartString - $cartString")
        val result = Gson().fromJson(cartString, CartUIState::class.java)
        Log.d("CartUIState", " fromStringToCartItem : result - $result")
        return result

    }catch(e : Exception){
        return null
    }
}