package com.client.headlineshay.di

import android.content.Context
import com.client.headlineshay.MyApplication
import com.client.headlineshay.R
import com.client.headlineshay.network.ApiKeyInterceptor
import com.client.headlineshay.network.api.NewsApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object RetrofitModule{
    //object:more like static



    @Singleton
    @Provides
    fun provideGsonBuilder() : Gson{
        return GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()//ignores fields not marked with @Expose
                .create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson:Gson, @ApplicationContext context: Context) : Retrofit.Builder{


        val client = OkHttpClient.Builder()
                .addInterceptor(ApiKeyInterceptor(context.getString(R.string.news_api_key)))


        return Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2/")

//            .client(OkHttpClient().newBuilder().addInterceptor(Interceptor {
//
//                val request = it.request()
//                val newRequest:Request.Builder =request.newBuilder()
//                    .addHeader("Connection","close")
//                    .header("Authorization","8833c0b1962c49a2b802549139ea04cd")
//
//                return it.proceed(newRequest.build())
//            }).build())
                .addConverterFactory(GsonConverterFactory.create())

    }


    @Singleton
    @Provides
    fun provideNewApi(retrofit: Retrofit.Builder) : NewsApi{
        return retrofit
                .build()
                .create(NewsApi::class.java)
    }

}