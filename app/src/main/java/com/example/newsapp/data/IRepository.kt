package com.example.newsapp.data

import com.example.newsapp.entity.News
import com.example.newsapp.entity.NewsData
import com.example.newsapp.entity.NewsInfo
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Header

interface IRepository {
    suspend fun testGetter()
    suspend fun getNewsList(): Flow<NewsInfo>
    suspend fun upsertToDatabase(news: List<News>)
}
