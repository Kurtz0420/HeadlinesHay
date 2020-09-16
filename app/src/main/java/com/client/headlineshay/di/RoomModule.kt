package com.client.headlineshay.di

import android.content.Context
import androidx.room.Room
import com.client.headlineshay.room.ArticlesDAO
import com.client.headlineshay.room.ArticlesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object RoomModule {


    @Singleton
    @Provides
    fun provideArticlesDB(@ApplicationContext context : Context) : ArticlesDatabase{
        return Room.databaseBuilder(
                context,
                ArticlesDatabase::class.java,
                ArticlesDatabase.DATABASE_NAME
        )
                .fallbackToDestructiveMigration()
                .build()
    }

    @Singleton
    @Provides
    fun provideArticlesDAO( articlesDatabase: ArticlesDatabase): ArticlesDAO{
        return articlesDatabase.articlesDAO()
    }

}