package com.example.newsapp.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newsapp.room.dao.NewsDao
import com.example.newsapp.room.entity.NewsEntity

@Database(entities = [NewsEntity::class], version = 1, exportSchema = false)
abstract class NewsDataBase : RoomDatabase() {

    abstract fun newsDao(): NewsDao
}