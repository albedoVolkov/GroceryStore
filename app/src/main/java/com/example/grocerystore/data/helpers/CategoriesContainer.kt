package com.example.grocerystore.data.helpers

import com.google.gson.annotations.SerializedName

data class CategoriesContainer @JvmOverloads constructor(
    @SerializedName("сategories")
    val items : List<CategoryUIState> = arrayListOf()
)