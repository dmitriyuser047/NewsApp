package com.example.newsapp.room.dao

import androidx.room.Dao
import androidx.room.Upsert
import com.example.newsapp.room.entity.NewsEntity

@Dao
interface NewsDao {
    @Upsert
    suspend fun upsertNews(newsEntity: NewsEntity)
}