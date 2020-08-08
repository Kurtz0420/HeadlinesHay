package com.client.headlineshay.repository

import com.client.headlineshay.network.api.NetworkMapper
import com.client.headlineshay.network.api.NewsApi
import com.client.headlineshay.network.models.local.ArticleLocal
import com.client.headlineshay.room.ArticlesDAO
import com.client.headlineshay.room.CacheMapper
import com.client.headlineshay.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class MainRepository
    constructor(
        private val articleDao:ArticlesDAO,
        private val newsApi : NewsApi,
        private val cacheMapper: CacheMapper,
        private val networkMapper: NetworkMapper

    ){


    /*Gets NewsArticles From APi
    * Converts it to Cache-able Objects
    * Stores Cache-able Objects in DB
    * Emits CachedObjects*/
    suspend fun getLatestNews() : Flow<DataState<List<ArticleLocal>>> = flow {

        emit(DataState.Loading)
        try{

            //gets items from api - List<ArticlesNetwork>
            val articlesNetwork = newsApi.getLatestNews("us","8833c0b1962c49a2b802549139ea04cd")
            //converts it to local - List<ArticleLocal>
            val articles = networkMapper.mapFromArticleNetworkList(articlesNetwork.articles)
            for(article in articles){
                //stores local -
                articleDao.insert(cacheMapper.mapFromArticleLocal(article))
            }
            //gets cachedArticles - List<ArticleCacheEntity>
            val cacheArticles = articleDao.getAllArticlesCached()
            //emits local - List<ArticleLocal>
            emit(DataState.Success(cacheMapper.mapFromArticleCacheEntityList(cacheArticles)))

        }catch (e:Exception){

        }
    }



}