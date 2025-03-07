package com.example.newsapp.entity

import com.squareup.moshi.Json

data class NewsInfo(
    val news: List<News>,
    val count: Int,
    @Json(name = "error_msg")
    val errorMsg: String
)