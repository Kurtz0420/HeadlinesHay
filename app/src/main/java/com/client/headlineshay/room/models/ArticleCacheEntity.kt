package com.client.headlineshay.room.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.OffsetDateTime
import java.util.*


@Entity(tableName = "articles", indices = [Index(value = ["title"], unique = true)])
data class ArticleCacheEntity(

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        var id:Int?,

        @ColumnInfo(name = "sourceId")
        var sourceId:String?,

        @ColumnInfo(name = "sourceName")
        var sourceName:String?,

        @ColumnInfo(name = "author")
        var author:String?,



        @ColumnInfo(name = "title")
        var title:String,

        @ColumnInfo(name = "description")
        var description:String?,

        @ColumnInfo(name = "url")
        var url:String?,

        @ColumnInfo(name = "urlToImage")
        var urlToImage:String?,

        @ColumnInfo(name = "publishedAt")
        var publishedAt:String?,

        @ColumnInfo(name = "content")
        var content:String?,

        @ColumnInfo(name = "dateRetrieved")
        var dateRetrieved: String?,

        @ColumnInfo(name = "country")
        var country: String?,

        @ColumnInfo(name = "category")
        var category: String?




){

}