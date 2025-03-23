package com.example.newsapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newsapp.data.database.dao.NewsDao
import com.example.newsapp.data.database.entity.NewsEntity

@Database(entities = [NewsEntity::class], version = 1, exportSchema = false)
abstract class NewsDataBase : RoomDatabase() {

    abstract fun newsDao(): NewsDao
}