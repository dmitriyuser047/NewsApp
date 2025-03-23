package com.example.newsapp.data

import com.example.newsapp.domain.model.NewsData
import retrofit2.http.GET

interface NewsApi {
    @GET("/api/mobile/news/list")
    suspend fun listNews(): NewsData
}