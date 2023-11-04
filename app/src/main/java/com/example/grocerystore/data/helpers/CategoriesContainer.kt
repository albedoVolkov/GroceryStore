package com.example.grocerystore.data.helpers

import com.example.grocerystore.data.helpers.UIstates.item.CategoryUIState
import com.google.gson.annotations.SerializedName

data class CategoriesContainer @JvmOverloads constructor(
    @SerializedName("categories")
    val items : List<CategoryUIState> = arrayListOf()
)