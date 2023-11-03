package com.example.grocerystore.data.helpers.UIstates.item

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson

@Entity(tableName = "addressesList")
data class AddressUIState(
    @PrimaryKey
    @ColumnInfo(name = "addressId")
    var addressId: String = "",
    @ColumnInfo(name = "fName")
    var fName: String = "",
    @ColumnInfo(name = "lName")
    var lName: String = "",
    @ColumnInfo(name = "countryISOCode")
    var countryISOCode: String = "",
    @ColumnInfo(name = "streetAddress")
    var streetAddress: String = "",
    @ColumnInfo(name = "streetAddress2")
    var streetAddress2: String = "",
    @ColumnInfo(name = "city")
    var city: String = "",
    @ColumnInfo(name = "state")
    var state: String = "",
    @ColumnInfo(name = "zipCode")
    var zipCode: String = "",
    @ColumnInfo(name = "phoneNumber")
    var phoneNumber: String = ""
){
    override fun toString(): String {
        return Gson().toJson(this,AddressUIState::class.java)
    }
}




fun fromStringToAddressItem(addressString: String): AddressUIState? {
    return try {
        Gson().fromJson(addressString, AddressUIState::class.java)
    }catch(e : Exception){
        null
    }
}

