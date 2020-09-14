package com.client.headlineshay.network.models.local

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class ArticleLocal(

        var pk : Int?,

        var sourceId : String?,

        var sourceName : String?,

        var author : String?,

        var title : String,

        var description : String?,

        var url : String?,

        var urlToImage : String?,

        var publishedAt : String?,

        var dateRetrieved : String?, // date of when data is retrieved to client

        var content : String?

){

}