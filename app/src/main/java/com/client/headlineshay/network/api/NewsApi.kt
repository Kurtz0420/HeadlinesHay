package com.client.headlineshay.network.api

import com.client.headlineshay.network.models.ResultsArticles
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi{


    // https://newsapi.org/v2   - base url

    @GET("top-headlines")
    suspend fun getLatestNews(@Query("country") country: String?) : ResultsArticles

}