package com.example.newsapp.ui.screens.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.Repository
import com.example.newsapp.entity.News
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _news = MutableLiveData<List<News>>()
    val news: LiveData<List<News>>
        get() = _news

    init {
        getListNews()
    }

    fun getListNews() {
        viewModelScope.launch {
            val newsLiveData = repository.getNewsList()
            newsLiveData.observeForever {
                _news.value = it
            }
        }
    }

    fun filterNews(news: List<News>, searchQuery: String): List<News> {
        return news.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
                    it.annotation.contains(searchQuery, ignoreCase = true)

        }
    }

    fun hideNews(news: News) {
        viewModelScope.launch {
            news.hiden = true
            repository.updateNews(news)
        }
    }
}