package com.example.grocerystore.domain.API

import com.example.grocerystore.domain.helpers.Section
import retrofit2.http.GET

interface SectionsAPI {
    @GET("058729bd-1402-4578-88de-265481fd7d54")
    fun getSections(): List<Section>
}