package com.example.newsapp.data.repository

import com.example.newsapp.data.database.dao.NewsDao
import com.example.newsapp.data.database.entity.NewsEntity
import com.example.newsapp.data.mapper.toNews
import com.example.newsapp.data.mapper.toNewsEntity
import com.example.newsapp.data.source.NewsApi
import com.example.newsapp.domain.model.News
import com.example.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi,
    private val newsDao: NewsDao
) : NewsRepository {

    override suspend fun getNewsList(showHiddenList: Boolean): Flow<List<News>> {
        fetchNewsFromApi()
        return newsDao.observeAllNews().map { entities ->
            filterNewsEntity(entities, showHiddenList)
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
            val changeHidden = existingNews?.hidden ?: new.hidden
            newsDao.upsertNews(new.toNewsEntity().copy(hidden = changeHidden))
        }
    }

    override suspend fun filterNewsEntity(
        newsEntity: List<NewsEntity>,
        showHiddenList: Boolean
    ): List<News> {
        return newsEntity
            .filter { it.hidden == showHiddenList }
            .map { new ->
                new.toNews()
            }
    }

    override suspend fun reformedNewsToEntity(news: News): NewsEntity {
        return news.toNewsEntity()
    }

    override suspend fun updateNews(news: News) {
        newsDao.upsertNews(reformedNewsToEntity(news))
    }
}