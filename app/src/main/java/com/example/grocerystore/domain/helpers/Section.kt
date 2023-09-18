package com.example.grocerystore.domain.helpers

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Section(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("name")
    @Expose
    var name: String,
    @SerializedName("image_url")
    @Expose
    var image_url : String
)