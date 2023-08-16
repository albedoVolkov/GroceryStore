package com.example.grocerystore.domain.helpers

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Categories(
    @SerializedName("сategories")
    @Expose
    val categories : List<Section>
    )