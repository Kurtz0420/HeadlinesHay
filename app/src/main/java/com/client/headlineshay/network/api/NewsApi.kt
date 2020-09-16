package com.client.headlineshay.network.api

import com.client.headlineshay.network.models.ResultsArticles
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NewsApi{


    @GET("everything")
    suspend fun searchNewsAbout(@Query("q") q: String?,
                                @Query("sources") sources: String?,
                                @Query("domains") domains: String?,
                                @Query("from") from: String?,
                                @Query("to") to: String?,
                                @Query("language") language: String?,
                                @Query("sortBy") sortBy: String?,
                                @Query("pageSize") pageSize: Int?,
                                @Query("page") page: Int?) : ResultsArticles

    @GET("top-headlines")
    suspend fun getTopHeadlines(@Query("category") category: String?,
                                @Query("country") country: String?,
                                @Query("sources") sources: String?,
                                @Query("q") q: String?,
                                @Query("pageSize") pageSize: Int?,
                                @Query("page") page: Int?) : ResultsArticles


}