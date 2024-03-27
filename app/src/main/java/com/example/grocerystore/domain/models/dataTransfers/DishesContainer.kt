package com.example.grocerystore.domain.models.dataTransfers

import com.example.grocerystore.domain.models.item.DishUIState
import com.google.gson.annotations.SerializedName

data class DishesContainer @JvmOverloads constructor(
    @SerializedName("dishes")
    val items : List<DishUIState> = arrayListOf()
)