package com.example.grocerystore.data.helpers.UIstates.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.grocerystore.data.helpers.UIstates.item.AddressUIState
import com.example.grocerystore.data.helpers.UIstates.item.CartUIState
import com.example.grocerystore.data.helpers.UIstates.item.OrderUIState
import com.example.grocerystore.data.source.local.utilsData.ListTypeConverter
import com.example.grocerystore.data.helpers.Utils
import com.google.gson.Gson

@Entity(tableName = "users")
data class UserUIState(
    @PrimaryKey
    @ColumnInfo(name = "userId")
    var userId: String = "",

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "image")
    var image: String = "",//link

    @ColumnInfo(name = "phone")
    var phone: String = "",

    @ColumnInfo(name = "email")
    var email: String = "",

    @ColumnInfo(name = "password")
    var password: String = "",

    @TypeConverters(ListTypeConverter::class)
    @ColumnInfo(name = "likes")
    var likes: List<String> = ArrayList(),

    @TypeConverters(ListTypeConverter::class)
    @ColumnInfo(name = "address")
    var address: List<AddressUIState> = ArrayList(),

    @TypeConverters(ListTypeConverter::class)
    @ColumnInfo(name = "cart")
    var cart: List<CartUIState> = ArrayList(),

    @TypeConverters(ListTypeConverter::class)
    @ColumnInfo(name = "orders")
    var orders: List<OrderUIState> = ArrayList(),

    @ColumnInfo(name = "userType")
    var userType: String = Utils.UserType.CUSTOMER.name,

    ){
        override fun toString(): String {
            return Gson().toJson(this,UserUIState::class.java)
        }

        fun toUserUIStateShort(): UserUIStateShort {
            return UserUIStateShort(this.userId ?: "",this.name ?: "",this.image ?: "")
        }
    }


fun fromStringToUserItem(userString: String): UserUIState? {
    return try {
        Gson().fromJson(userString, UserUIState::class.java)
    }catch(e : Exception){
        null
    }
}

