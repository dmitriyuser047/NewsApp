package com.example.newsapp.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.newsapp.room.entity.NewsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Upsert
    suspend fun upsertNews(newsEntity: NewsEntity)

    @Query("SELECT * FROM newsentity WHERE hidden = 0 ORDER BY newsDate DESC")
    fun observeAllNews(): Flow<List<NewsEntity>>

    @Query("SELECT * FROM newsentity WHERE id = :id")
    suspend fun getNewsById(id: Int): NewsEntity?
}