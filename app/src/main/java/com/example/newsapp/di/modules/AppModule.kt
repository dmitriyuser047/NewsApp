package com.example.newsapp.di.modules

import android.content.Context
import androidx.room.Room
import com.example.newsapp.data.NewsApi
import com.example.newsapp.data.database.NewsDataBase
import com.example.newsapp.data.database.dao.NewsDao
import com.example.newsapp.data.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): NewsDataBase {
        return Room.databaseBuilder(
            appContext,
            NewsDataBase::class.java,
            "room_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDao(newsDataBase: NewsDataBase): NewsDao {
        return newsDataBase.newsDao()
    }

    @Singleton
    @Provides
    fun provideRepository(newsDao: NewsDao, newsApi: NewsApi): Repository {
        return Repository(newsApi, newsDao)
    }

}