package com.example.grocerystore.data.helpers.UIstates.item

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
@Entity(tableName = "dishesList")
data class DishUIState @JvmOverloads constructor(
    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(name = "id")
    var id: Int = -1,

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
)
