package com.example.grocerystore.data.source.remove.api

import com.example.grocerystore.data.helpers.CategoriesContainer
import retrofit2.http.GET
import retrofit2.http.Path

interface CategoriesAPI {
    @GET("{categoryList}")
    suspend fun getCategoriesList(@Path("categoryList") categoryList : String):  CategoriesContainer?

}