package com.client.headlineshay.network.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ArticleSource(

    @SerializedName("id")
    @Expose
    var id : String?,

    @SerializedName("name")
    @Expose
    var name : String?

){

}