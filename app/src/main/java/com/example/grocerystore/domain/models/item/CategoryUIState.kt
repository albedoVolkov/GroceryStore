package com.example.grocerystore.domain.models.item


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.grocerystore.domain.utils.ConstantsDomain
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

@Entity(tableName = ConstantsDomain.CATEGORIES_ROOM_TABLE_NAME)
data class CategoryUIState @JvmOverloads constructor(
    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(name = "id")
    var id: String = "-1",

    @SerializedName("name")
    @ColumnInfo(name = "name")
    var name: String = "",

    @SerializedName("image")
    @ColumnInfo(name = "image")
    var image: String = ""
){
    override fun toString(): String {
        return Gson().toJson(this, CategoryUIState::class.java)
    }
}

fun fromStringToCategoryItem(categoryString: String): CategoryUIState? {
    return try {
        Gson().fromJson(categoryString, CategoryUIState::class.java)
    }catch(e : Exception){
        null
    }
}