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

    /*Data retrieved from the network
    * Sent to the cache
    * Retrieved from the cache
    * Emit the cache*/
    suspend fun getLatestNews() : Flow<DataState<List<ArticleLocal>>> = flow {


        emit(DataState.Loading)
        try{

            //gets items from api - List<ArticlesNetwork>
            val articlesResult = newsApi.getLatestNews("bitcoin","8833c0b1962c49a2b802549139ea04cd", 1)
            //converts it to local - List<ArticleLocal>
            val articles = networkMapper.mapFromArticleNetworkList(articlesResult.articles)
            for(article in articles){
                //stores local -
                articleDao.insert(cacheMapper.mapFromArticleLocal(article))
            }
            //gets cachedArticles - List<ArticleCacheEntity>
            val cacheArticles = articleDao.getAllArticlesCached()
            //emits local - List<ArticleLocal>
            emit(DataState.Success(cacheMapper.mapFromArticleCacheEntityList(cacheArticles)))

        }catch (e:Exception){
            emit(DataState.Error(e))
        }
    }



}