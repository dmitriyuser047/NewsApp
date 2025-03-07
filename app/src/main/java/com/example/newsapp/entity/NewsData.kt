package com.example.newsapp.entity

import com.squareup.moshi.Json

data class NewsData(
    val success: Boolean,
    @Json(name = "data")
    val newsData: NewsInfo
)
