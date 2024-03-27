package com.example.grocerystore.data.source.remove.api

import com.example.grocerystore.data.utils.ConstantsData
import com.example.grocerystore.domain.models.dataTransfers.CategoriesContainer
import retrofit2.http.GET

interface CategoriesAPI {
    @GET(ConstantsData.END_CATEGORIES_URL_LINK)
    suspend fun getCategoriesList(): CategoriesContainer?

}