package com.example.grocerystore.data.source.remove.api


import com.example.grocerystore.domain.models.dataTransfers.DishesContainer
import retrofit2.http.GET
import retrofit2.http.Path

interface DishesAPI {

    @GET("{dishes}")
    suspend fun getDishesList(@Path("dishes") dishesList : String): DishesContainer?
}