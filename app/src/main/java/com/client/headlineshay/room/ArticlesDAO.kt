package com.client.headlineshay.room

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.client.headlineshay.room.models.ArticleCacheEntity


@Dao
interface ArticlesDAO{


    @Insert(onConflict = OnConflictStrategy.IGNORE)//replaces the data on conflict
    suspend fun insert(articleCacheEntity: ArticleCacheEntity) : Long //Long:the column it has been inserted


    @Query("SELECT * FROM articles ORDER BY id ASC LIMIT :pageNo")
    suspend fun getAllArticlesCached(pageNo : Int): List<ArticleCacheEntity>

    @Query("SELECT * FROM articles ORDER BY id ASC LIMIT 1")
    suspend fun getLatestArticleCached(): List<ArticleCacheEntity>

//    @Query("SELECT * FROM articles ORDER BY id DESC")
//    suspend fun getAllArticlesCachedPaged(): DataSource.Factory<Int, ArticleCacheEntity>

}