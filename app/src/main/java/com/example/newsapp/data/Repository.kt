package com.example.newsapp.data

import com.example.newsapp.App
import com.example.newsapp.entity.News
import com.example.newsapp.entity.NewsInfo
import com.example.newsapp.room.entity.NewsEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Singleton
class Repository : IRepository {

    private val component = App.appComponent
    private val newsApi = component.newsApi()
    private val newsDao = App.newsDataBase.newsDao()

    override suspend fun testGetter() {
        val news = newsApi.listNews()
        upsertToDatabase(news.newsData.news)
    }

    override suspend fun getNewsList(): Flow<NewsInfo> {
        TODO()
    }

    override suspend fun upsertToDatabase(news: List<News>) {
        news.forEach { new ->
            newsDao.upsertNews(
                NewsEntity(
                    id = new.id,
                    title = new.title,
                    img = new.img,
                    localImg = new.localImg,
                    newsDate = new.newsDate,
                    annotation = new.annotation,
                    idResource = new.idResource,
                    type = new.type,
                    newsDateUts = new.newsDateUts,
                    mobileUrl = new.mobileUrl
                )
            )
        }
    }
}