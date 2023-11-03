package com.example.grocerystore.data.helpers.UIstates.item

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
    return try {
        Gson().fromJson(cartString, CartUIState::class.java)
    }catch(e : Exception){
        null
    }
}