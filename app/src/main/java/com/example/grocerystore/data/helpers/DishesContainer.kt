package com.example.grocerystore.data.helpers

import com.google.gson.annotations.SerializedName

data class DishesContainer @JvmOverloads constructor(
    @SerializedName("dishes")
    val items : List<DishUIState> = arrayListOf()
)