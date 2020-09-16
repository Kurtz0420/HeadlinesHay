package com.client.headlineshay.network.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResultsArticles(

        @SerializedName("status")
        @Expose
        var status : String,

        @SerializedName("totalResults")
        @Expose
        var totalResults : Int,

        @SerializedName("articles")
        @Expose
        var articles : List<ArticleNetwork>

){

}