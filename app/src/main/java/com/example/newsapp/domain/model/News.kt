package com.example.newsapp.domain.model

import com.squareup.moshi.Json

data class News(
    val id: Int,
    val title: String,
    val img: String,
    @Json(name = "local_img")
    val localImg: String,
    @Json(name = "news_date")
    val newsDate: String,
    val annotation: String,
    @Json(name = "id_resource")
    val idResource: Int,
    val type: Int,
    @Json(name = "news_date_uts")
    val newsDateUts: String,
    @Json(name = "mobile_url")
    val mobileUrl: String,
    @Json(ignore = true)
    override var hidden: Boolean? = false
) : INews {
}
