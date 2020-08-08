package com.client.headlineshay.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.client.headlineshay.room.models.ArticleCacheEntity


@Dao
interface ArticlesDAO{


    @Insert(onConflict = OnConflictStrategy.REPLACE)//replaces the data on conflict
    suspend fun insert(articleCacheEntity: ArticleCacheEntity) : Long //Long:the column it has been inserted


    @Query("SELECT * FROM articles")
    suspend fun getAllArticlesCached(): List<ArticleCacheEntity>

}