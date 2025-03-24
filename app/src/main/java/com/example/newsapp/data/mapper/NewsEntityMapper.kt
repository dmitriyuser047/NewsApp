package com.example.newsapp.data.mapper

import com.example.newsapp.data.database.entity.NewsEntity
import com.example.newsapp.domain.model.News

fun NewsEntity.toNews() = News(
    id,
    title,
    img,
    localImg,
    newsDate,
    annotation,
    idResource,
    type,
    newsDateUts,
    mobileUrl,
    hidden
)

fun News.toNewsEntity() = NewsEntity(
    id,
    title,
    img,
    localImg,
    newsDate,
    annotation,
    idResource,
    type,
    newsDateUts,
    mobileUrl,
    hidden
)