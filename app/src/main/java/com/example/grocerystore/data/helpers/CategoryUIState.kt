package com.example.grocerystore.data.helpers

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "categoriesList")
data class CategoryUIState @JvmOverloads constructor(
    @PrimaryKey(true)
    @SerializedName("id")
    @ColumnInfo(name = "id")
    var id: Int = -1,

    @SerializedName("name")
    @ColumnInfo(name = "name")
    var name: String = "",

    @SerializedName("image_url")
    @ColumnInfo(name = "image_url")
    var image : String = ""
)