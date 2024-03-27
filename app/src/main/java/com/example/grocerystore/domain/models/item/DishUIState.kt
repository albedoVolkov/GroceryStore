package com.example.grocerystore.domain.models.item

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.grocerystore.domain.utils.ConstantsDomain
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
@Entity(tableName = ConstantsDomain.DISHES_ROOM_TABLE_NAME)
data class DishUIState @JvmOverloads constructor(
    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(name = "id")
    var id: String = "-1",

    @SerializedName("name")
    @ColumnInfo(name = "name")
    var name: String = "",

    @SerializedName("price")
    @ColumnInfo(name = "price")
    var price: Int = -1,

    @SerializedName("weight")
    @ColumnInfo(name = "weight")
    var weight: Int = -1,

    @SerializedName("description")
    @ColumnInfo(name = "description")
    var description: String = "",

    @SerializedName("image_url")
    @ColumnInfo(name = "image_url")
    var image: String = "",

    @SerializedName("tegs")
    @ColumnInfo(name = "tags")
    var tags: List<String> = ArrayList()
){
    override fun toString(): String {
        return Gson().toJson(this, DishUIState::class.java)
    }
}


fun fromStringToDishItem(dishString: String): DishUIState? {
    return try {
        Gson().fromJson(dishString, DishUIState::class.java)
    }catch(e : Exception){
        null
    }
}