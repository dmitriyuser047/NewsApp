package com.example.newsapp.data

import androidx.lifecycle.LiveData
import com.example.newsapp.entity.News
import com.example.newsapp.room.entity.NewsEntity

interface IRepository {
    suspend fun getNewsList(): LiveData<List<News>>
    suspend fun upsertToDatabase(news: List<News>)
    suspend fun fetchNewsFromApi()
    suspend fun updateNews(news: News)
    fun reformedNewsToEntity(news: News): NewsEntity
    fun reformedNewsEntity(newsEntity: List<NewsEntity>): List<News>
}
