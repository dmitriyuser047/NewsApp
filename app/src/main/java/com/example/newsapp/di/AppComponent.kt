package com.example.newsapp.di

import android.content.Context
import com.example.newsapp.data.NewsApi
import com.example.newsapp.data.Repository
import com.example.newsapp.ui.screens.news.NewsViewModel
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class, NetworkModule::class]
)
interface AppComponent {
    fun context(): Context
    fun repository(): Repository
    fun retrofit(): Retrofit
    fun newsApi(): NewsApi
    fun newsViewModel(): NewsViewModel
}