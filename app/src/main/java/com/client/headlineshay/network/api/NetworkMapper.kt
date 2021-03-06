package com.client.headlineshay.network.api

import com.client.headlineshay.network.models.ArticleNetwork
import com.client.headlineshay.network.models.ArticleSource
import com.client.headlineshay.network.models.local.ArticleLocal
import com.client.headlineshay.utils.EntityMapper
import com.client.headlineshay.utils.TimeConversions
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


/*Class Responsible for converting to and from ArticleNetwork(Article Fetched From Network) - ArticleLocal*/

class NetworkMapper
@Inject
constructor() : EntityMapper<ArticleNetwork, ArticleLocal> {


    override fun mapToArticleLocal(entity: ArticleNetwork): ArticleLocal {

        return ArticleLocal(

                pk = null,
                sourceId = entity.articleSource.id,
                sourceName = entity.articleSource.name,
                author = entity.author,
                title = entity.title,
                description = entity.description,
                url = entity.url,
                urlToImage = entity.urlToImage,
                publishedAt = entity.publishedAt,
                content = entity.content,
                dateRetrieved = entity.dateRetrieved //saves the date, when it is fetched

        )
    }

    override fun mapFromArticleLocal(domainModel: ArticleLocal): ArticleNetwork {


        return ArticleNetwork(

                articleSource = ArticleSource(domainModel.sourceId, domainModel.sourceName),
                author = domainModel.author,
                title = domainModel.title,
                description = domainModel.description,
                url = domainModel.url,
                urlToImage = domainModel.urlToImage,
                publishedAt = domainModel.publishedAt,
                content = domainModel.content,
                dateRetrieved = domainModel.dateRetrieved

        )
    }

    /*Takes a list of articleNetwork, returns a list of ArticleLocal*/
    fun mapFromArticleNetworkList(articlesNetwork: List<ArticleNetwork>): List<ArticleLocal> {
        return articlesNetwork.map { mapToArticleLocal(it) }
    }

}