package com.example.newsapp.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.newsapp.data.database.entity.NewsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Upsert
    suspend fun upsertNews(newsEntity: NewsEntity)

    @Query("SELECT * FROM newsentity ORDER BY newsDate DESC")
    fun observeAllNews(): Flow<List<NewsEntity>>

    @Query("SELECT * FROM newsentity WHERE id = :id")
    suspend fun getNewsById(id: Int): NewsEntity?

    @Query("SELECT * FROM newsentity ORDER BY newsDate DESC")
    suspend fun getNewsList(): List<NewsEntity>
}