package com.example.grocerystore.data.source.remove.retrofit

import com.example.grocerystore.data.source.remove.api.CategoriesAPI
import com.example.grocerystore.data.source.remove.api.DishesAPI
import com.example.grocerystore.data.utils.ConstantsSource
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitDataSource {
    private val retrofit by lazy {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        Retrofit.Builder()
            .baseUrl(ConstantsSource().START_URL_LINK).client(client)//"https://run.mocky.io"//ConstantsSource().START_URL_LINK
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
        val categoriesAPI : CategoriesAPI by lazy {
            retrofit.create(CategoriesAPI::class.java)
        }

        val dishesAPI : DishesAPI by lazy {
            retrofit.create(DishesAPI::class.java)
        }
}