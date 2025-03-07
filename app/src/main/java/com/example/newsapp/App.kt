package com.example.newsapp

import android.app.Application
import com.example.newsapp.di.AppComponent
import com.example.newsapp.di.AppModule
import com.example.newsapp.di.DaggerAppComponent
import com.example.newsapp.room.database.NewsDataBase

open class App: Application() {
    companion object {
        lateinit var appComponent: AppComponent
        lateinit var newsDataBase: NewsDataBase
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
        newsDataBase = NewsDataBase.getDatabase(this)
    }
}