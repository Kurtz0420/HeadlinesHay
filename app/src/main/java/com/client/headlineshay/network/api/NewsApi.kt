package com.client.headlineshay.network.api

import com.client.headlineshay.network.models.ResultsArticles
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NewsApi{


    // https://newsapi.org/v2   - base url
//    @Query("pageSize") pageSize: Int?

    @GET("everything")
    suspend fun getLatestNews(@Query("q") country: String?, @Query("apiKey") apiKey: String?, @Query("page") pageNo: Int?) : ResultsArticles

}