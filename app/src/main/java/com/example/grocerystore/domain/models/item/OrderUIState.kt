package com.example.grocerystore.domain.models.item

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.grocerystore.domain.utils.ConstantsDomain
import com.google.gson.Gson
import java.util.Date

@Entity(tableName = ConstantsDomain.ORDERS_ROOM_TABLE_NAME)
data class OrderUIState(
    @PrimaryKey
    @ColumnInfo(name = "orderId")
    var orderId: String = "",

    @ColumnInfo(name = "customerId")
    var customerId: String = "",

    @ColumnInfo(name = "items")
    var items: List<CartUIState> = ArrayList(),

    @ColumnInfo(name = "itemsPrices")
    var itemsPrices: Map<Int, Double> = mapOf(),

    @ColumnInfo(name = "deliveryAddress")
    var deliveryAddress: AddressUIState = AddressUIState(),

    @ColumnInfo(name = "shippingCharges")
    var shippingCharges: Double = 0.0,

    @ColumnInfo(name = "paymentMethod")
    var paymentMethod: String = "",

    @ColumnInfo(name = "status")
    var orderDate: Date = Date(),

    @ColumnInfo(name = "status")
    var status: String = "" //Utils.OrderStatus.CONFIRMED.name
){
    override fun toString(): String {
        return Gson().toJson(this, OrderUIState::class.java)
    }
}


fun fromStringToOrderItem(orderString: String): OrderUIState? {
    return try{
        Gson().fromJson(orderString, OrderUIState::class.java)
    }catch(e : Exception){
        null
    }
}