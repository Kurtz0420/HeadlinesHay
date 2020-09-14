package com.client.headlineshay.room

import com.client.headlineshay.network.models.local.ArticleLocal
import com.client.headlineshay.room.models.ArticleCacheEntity
import com.client.headlineshay.utils.AppPreferences
import com.client.headlineshay.utils.EntityMapper
import java.util.*
import javax.inject.Inject


/*Responsible For Converting ArticleLocal to ArticleCacheEntity and vice versa*/

class CacheMapper
@Inject
constructor() : EntityMapper<ArticleCacheEntity, ArticleLocal>{


    override fun mapToArticleLocal(entity: ArticleCacheEntity): ArticleLocal {
        return ArticleLocal(

                pk = entity.id,
                sourceId = entity.sourceId,
                sourceName = entity.sourceName,
                author = entity.author,
                title = entity.title,
                description = entity.description,
                url = entity.url,
                urlToImage = entity.urlToImage,
                publishedAt = entity.publishedAt,
                content = entity.content,
                dateRetrieved = entity.dateRetrieved
        )
    }

    override fun mapFromArticleLocal(articleLocal: ArticleLocal): ArticleCacheEntity {


        return ArticleCacheEntity(

                id = null,
                sourceId = articleLocal.sourceId,
                sourceName = articleLocal.sourceName,
                author = articleLocal.author,
                title = articleLocal.title,
                description = articleLocal.description,
                url = articleLocal.url,
                urlToImage = articleLocal.urlToImage,
                publishedAt = articleLocal.publishedAt,
                content = articleLocal.content,
                dateRetrieved = articleLocal.dateRetrieved,
                country = AppPreferences.news_country,
                category = AppPreferences.news_category
        )
    }


    fun mapFromArticleCacheEntityList(articleCacheEntities : List<ArticleCacheEntity>) : List<ArticleLocal>{
        return articleCacheEntities.map {mapToArticleLocal(it)}
    }




}