package com.example.newsapp.di

import com.example.newsapp.data.NewsApi
import com.example.newsapp.ui.screens.news.NewsViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class, NetworkModule::class]
)
interface AppComponent {
    fun newsApi(): NewsApi
    fun newsViewModel(): NewsViewModel
}