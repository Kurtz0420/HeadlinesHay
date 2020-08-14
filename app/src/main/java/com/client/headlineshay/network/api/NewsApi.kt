package com.client.headlineshay.network.api

import com.client.headlineshay.network.models.ResultsArticles
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NewsApi{


    // https://newsapi.org/v2   - base url
//    @Query("pageSize") pageSize: Int?
//    , @Query("apiKey") apiKey: String?


    /*Get Everything
    * (@Query("q") q: String?,
@Query("sources") sources: String?,
@Query("domains") domains: String?,
@Query("from") from: String?,
@Query("to") to: String?,
@Query("language") language: String?,
@Query("sortBy") sortBy: String?,
@Query("pageSize") pageSize: Int?,
@Query("page") page: Int?)*/



    /*Get Top Headlines
    * @Query("category") category: String?,
@Query("country") country: String?,
@Query("sources") sources: String?,
@Query("q") q: String?,
@Query("pagesize") pageSize: Int?,
@Query("page") page: Int?*/


    /*Get Sources
    *@Query("category") category: String?,
@Query("language") language: String?,
@Query("country") country: String?*/



    @GET("everything")
    suspend fun getEverything(@Query("q") q: String?,
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

    @GET("everything")
    suspend fun getLatestNews(@Query("q") country: String?, @Query("page") pageNo: Int?) : ResultsArticles

}