package com.example.newsapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.newsapp.App
import com.example.newsapp.entity.News
import com.example.newsapp.room.entity.NewsEntity
import javax.inject.Singleton

@Singleton
class Repository : IRepository {

    private val component = App.appComponent
    private val newsApi = component.newsApi()
    private val newsDao = App.newsDataBase.newsDao()

    override suspend fun getNewsList(): LiveData<List<News>> {
        val newsFromDb = newsDao.observeAllNews()
        fetchNewsFromApi()
        return newsFromDb.map { newsEntities ->
            reformedNewsEntity(newsEntities)
        }
    }

    override suspend fun fetchNewsFromApi() {
        try {
            val gotNews = newsApi.listNews().newsData.news
            upsertToDatabase(gotNews)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun upsertToDatabase(news: List<News>) {
        news.forEach { new ->
            val existingNews = newsDao.getNewsById(new.id)
            val hidden = existingNews?.hiden ?: new.hiden
            println("hidden $existingNews = $hidden")
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
                    mobileUrl = new.mobileUrl,
                    hiden = hidden
                )
            )
        }
    }

    override fun reformedNewsEntity(newsEntity: List<NewsEntity>): List<News> {
        return newsEntity.map { new ->
            News(
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
        }
    }

    override fun reformedNewsToEntity(news: News): NewsEntity {
        return NewsEntity(
            id = news.id,
            title = news.title,
            img = news.img,
            localImg = news.localImg,
            newsDate = news.newsDate,
            annotation = news.annotation,
            idResource = news.idResource,
            type = news.type,
            newsDateUts = news.newsDateUts,
            mobileUrl = news.mobileUrl,
            hiden = news.hiden
        )
    }

    override suspend fun updateNews(news: News) {
        newsDao.upsertNews(reformedNewsToEntity(news))
    }
}