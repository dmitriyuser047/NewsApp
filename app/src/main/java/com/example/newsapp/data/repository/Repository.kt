package com.example.newsapp.data.repository

import com.example.newsapp.data.NewsApi
import com.example.newsapp.data.database.dao.NewsDao
import com.example.newsapp.data.database.entity.NewsEntity
import com.example.newsapp.domain.model.News
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val newsApi: NewsApi,
    private val newsDao: NewsDao
) : IRepository {

    override suspend fun getNewsList(showHiddenList: Boolean): Flow<List<News>> {
        fetchNewsFromApi()
        return newsDao.observeAllNews().map { entities ->
            reformedNewsEntity(entities, showHiddenList)
        }
    }

    override suspend fun fetchNewsFromApi() {
        try {
            val gotNews = newsApi.listNews().newsData.news
            println(gotNews)
            upsertToDatabase(gotNews)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun upsertToDatabase(news: List<News>) {
        news.forEach { new ->
            val existingNews = newsDao.getNewsById(new.id)
            val hidden = existingNews?.hidden ?: new.hidden
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
                    hidden = hidden
                )
            )
        }
    }

    override suspend fun reformedNewsEntity(
        newsEntity: List<NewsEntity>,
        showHiddenList: Boolean
    ): List<News> {
        return newsEntity
            .filter { it.hidden == showHiddenList }
            .map { new ->
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

    override suspend fun reformedNewsToEntity(news: News): NewsEntity {
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
            hidden = news.hidden
        )
    }

    override suspend fun updateNews(news: News) {
        newsDao.upsertNews(reformedNewsToEntity(news))
    }
}