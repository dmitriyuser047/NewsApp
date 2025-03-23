package com.example.newsapp.domain.model

import com.squareup.moshi.Json

data class NewsData(
    val success: Boolean,
    @Json(name = "data")
    val newsData: NewsInfo
)
