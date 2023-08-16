package com.example.grocerystore.data.source.remove.api


import com.example.grocerystore.data.helpers.DishesContainer
import retrofit2.http.GET
import retrofit2.http.Path

interface DishesAPI {

    @GET("{dishesList}")
    suspend fun getDishesList(@Path("dishesList") dishesList : String): DishesContainer?
}