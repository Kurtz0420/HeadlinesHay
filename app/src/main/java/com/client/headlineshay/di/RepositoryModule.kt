package com.client.headlineshay.di

import com.client.headlineshay.network.api.NetworkMapper
import com.client.headlineshay.network.api.NewsApi
import com.client.headlineshay.repository.MainRepository
import com.client.headlineshay.room.ArticlesDAO
import com.client.headlineshay.room.CacheMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule{


    @Singleton
    @Provides
    fun provideMainRepository(
        articlesDAO: ArticlesDAO,
        newsApi: NewsApi,
        cacheMapper: CacheMapper,
        networkMapper: NetworkMapper
    ):MainRepository{
        return MainRepository(articlesDAO, newsApi, cacheMapper, networkMapper)
    }

}