package com.client.headlineshay.room.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "articles")
data class ArticleCacheEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id:Int,

    @ColumnInfo(name = "sourceId")
    var sourceId:Int,

    @ColumnInfo(name = "sourceName")
    var sourceName:String,

    @ColumnInfo(name = "author")
    var author:String,

    @ColumnInfo(name = "title")
    var title:String,

    @ColumnInfo(name = "description")
    var description:String,

    @ColumnInfo(name = "url")
    var url:String,

    @ColumnInfo(name = "urlToImage")
    var urlToImage:String,

    @ColumnInfo(name = "publishedAt")
    var publishedAt:String,

    @ColumnInfo(name = "content")
    var content:String




){

}