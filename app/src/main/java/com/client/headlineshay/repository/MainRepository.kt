package com.client.headlineshay.repository

import android.util.Log
import com.client.headlineshay.network.api.NetworkMapper
import com.client.headlineshay.network.api.NewsApi
import com.client.headlineshay.network.enums.Language
import com.client.headlineshay.network.enums.SortBy
import com.client.headlineshay.network.models.ArticleNetwork
import com.client.headlineshay.network.models.local.ArticleLocal
import com.client.headlineshay.room.ArticlesDAO
import com.client.headlineshay.room.ArticlesDatabase
import com.client.headlineshay.room.CacheMapper
import com.client.headlineshay.room.models.ArticleCacheEntity
import com.client.headlineshay.utils.AppPreferences
import com.client.headlineshay.utils.DataState
import com.client.headlineshay.utils.TimeConversions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class MainRepository
constructor(
        private val articleDao:ArticlesDAO,
        private val newsApi : NewsApi,
        private val cacheMapper: CacheMapper,
        private val networkMapper: NetworkMapper,
        private val articlesDatabase: ArticlesDatabase // for clearing tables
){

    private val pageSize = 20


    /*Data retrieved from the network
    * Sent to the cache
    * Retrieved from the cache
    * Emit the cache*/
    suspend fun getLatestNews(pageNo:Int?) : Flow<DataState<List<ArticleLocal>>> = flow {

        emit(DataState.Loading)
        try{

            //if data is older than 24 hours, or Configs{country,category} is changed,  clear database for
            //fresh feeds
            clearDatabaseIf();


            //gets items from api - List<ArticlesNetwork>
            val articlesResult = newsApi.getTopHeadlines(category = AppPreferences.news_category, country = AppPreferences.news_country,
                    pageSize = pageSize, page = pageNo, sources = "", q= "")

            addDateRetrieved(articlesResult.articles)


            //converts it to local - List<ArticleLocal>
            val articles = networkMapper.mapFromArticleNetworkList(articlesResult.articles)
            for(article in articles){
                //stores local -
                //if data/object already exists, ignore
                articleDao.insert(cacheMapper.mapFromArticleLocal(article))
            }
            //gets cachedArticles - List<ArticleCacheEntity>
            val cacheArticles = articleDao.getAllArticlesCached(pageNo!! * pageSize)
            Log.d("MainRepository", "getLatestNews: ${cacheArticles.size}")
//            paginateList(cacheArticles)
            //emits local - List<ArticleLocal>
            emit(DataState.Success(cacheMapper.mapFromArticleCacheEntityList(cacheArticles)))

        }catch (e:Exception){
            emit(DataState.Error(e))
        }
    }

    /*Adds date retrieved field, for checking the lifetime of articles since fetched*/
    private fun addDateRetrieved(articles: List<ArticleNetwork>) {

        for(article in articles){
            article.dateRetrieved = Date().toString()
        }
    }

    private fun paginateList(cacheArticles: List<ArticleCacheEntity>) {

    }


    private suspend fun clearDatabaseIf() {
        val latestArticle  = articleDao.getLatestArticleCached()
        if(latestArticle.isNullOrEmpty()) return //for app first run

        /*If, last request older than 24hrs*/
        val lastArticleFetchedDate = TimeConversions.stringToDate(latestArticle[0].dateRetrieved)
        val dateTimeNow = Date()
        if(TimeConversions.getDifferenceIn(lastArticleFetchedDate, dateTimeNow, "hh").toLong() > 24){
            //data is older than 24 hours
            clearDatabaseCache()
        }

        /*If country config changed*/
        if(!latestArticle[0].country.equals(AppPreferences.news_country) && !latestArticle[0].category.equals(AppPreferences.news_category)){
            clearDatabaseCache()
        }
    }

    suspend fun searchNews(query:String?, pageNo:Int?) : Flow<DataState<List<ArticleLocal>>> = flow {


        emit(DataState.Loading)
        try{

            //gets items from api - List<ArticlesNetwork>
            val articlesResult = newsApi.getEverything(q= query, sources = "", domains = null, from = ""
                    ,to = "", language = com.client.headlineshay.network.enums.Language.ENGLISH.value, sortBy = SortBy.PUBLISHED_AT.value, pageSize = 20, page = pageNo)

            //converts it to local - List<ArticleLocal>
            val articles = networkMapper.mapFromArticleNetworkList(articlesResult.articles)

            //emits local - List<ArticleLocal>
            emit(DataState.Success(articles))

        }catch (e:Exception){
            emit(DataState.Error(e))
        }
    }


    fun clearDatabaseCache(){
        GlobalScope.launch(Dispatchers.IO) {
            articlesDatabase.clearAllTables()
        }
    }



}