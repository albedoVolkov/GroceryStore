package com.example.grocerystore.domain.models.dataTransfers

import com.example.grocerystore.domain.models.item.CategoryUIState
import com.google.gson.annotations.SerializedName

data class CategoriesContainer @JvmOverloads constructor(
    @SerializedName("categories")
    val items : List<CategoryUIState> = arrayListOf()
)