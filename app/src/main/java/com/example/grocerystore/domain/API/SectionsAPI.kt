package com.example.grocerystore.domain.API

import com.example.grocerystore.domain.helpers.Categories
import retrofit2.http.GET

interface SectionsAPI {
    @GET("v3/058729bd-1402-4578-88de-265481fd7d54")
    suspend fun getSections(): Categories
}