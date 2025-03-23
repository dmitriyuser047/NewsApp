package com.example.newsapp.data.repository

import com.example.newsapp.data.database.entity.NewsEntity
import com.example.newsapp.domain.model.News
import kotlinx.coroutines.flow.Flow

interface IRepository {
    suspend fun getNewsList(showHiddenList: Boolean): Flow<List<News>>
    suspend fun upsertToDatabase(news: List<News>)
    suspend fun fetchNewsFromApi()
    suspend fun updateNews(news: News)
    suspend fun reformedNewsToEntity(news: News): NewsEntity
    suspend fun reformedNewsEntity(
        newsEntity: List<NewsEntity>,
        showHiddenList: Boolean
    ): List<News>
}
