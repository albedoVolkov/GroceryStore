package com.example.grocerystore.data.helpers

import com.example.grocerystore.data.helpers.UIstates.item.DishUIState
import com.google.gson.annotations.SerializedName

data class DishesContainer @JvmOverloads constructor(
    @SerializedName("dishes")
    val items : List<DishUIState> = arrayListOf()
)