package com.client.headlineshay.room

import com.client.headlineshay.network.models.ArticleNetwork
import com.client.headlineshay.network.models.ArticleSource
import com.client.headlineshay.room.models.ArticleCacheEntity
import com.client.headlineshay.utils.EntityMapper
import javax.inject.Inject


/*Responsible For Converting ArticleNetwork to ArticleCacheEntity and vice versa*/

class CacheMapper
@Inject
constructor() : EntityMapper<ArticleCacheEntity, ArticleNetwork>{


    override fun mapFromArticleNetwork(entity: ArticleCacheEntity): ArticleNetwork {
        return ArticleNetwork(

            articleSource = ArticleSource(entity.sourceId, entity.sourceName),
            author = entity.author,
            title = entity.title,
            description = entity.description,
            url = entity.url,
            urlToImage = entity.urlToImage,
            publishedAt = entity.publishedAt,
            content = entity.content

        )
    }

    override fun mapToArticleNetwork(articleNetwork: ArticleNetwork): ArticleCacheEntity {


        return ArticleCacheEntity(

            id = 0,
            sourceId = articleNetwork.articleSource.id,
            sourceName = articleNetwork.articleSource.name,
            author = articleNetwork.author,
            title = articleNetwork.title,
            description = articleNetwork.description,
            url = articleNetwork.url,
            urlToImage = articleNetwork.urlToImage,
            publishedAt = articleNetwork.publishedAt,
            content = articleNetwork.content

        )
    }


    fun mapFromArticleCacheEntityList(articleCacheEntities : List<ArticleCacheEntity>) : List<ArticleNetwork>{
        return articleCacheEntities.map {mapFromArticleNetwork(it)}
    }

}