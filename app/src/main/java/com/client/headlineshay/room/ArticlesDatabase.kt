package com.client.headlineshay.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.client.headlineshay.room.models.ArticleCacheEntity


@Database(entities = [ArticleCacheEntity::class], version = 1)
abstract class ArticlesDatabase : RoomDatabase(){

    abstract fun articlesDAO() : ArticlesDAO


    companion object{
        val DATABASE_NAME:String = "ArticlesDB"
    }
}
