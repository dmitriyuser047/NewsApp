package com.example.newsapp.ui.screens.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.Repository
import com.example.newsapp.entity.News
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _news = MutableStateFlow(emptyList<News>())
    val news: StateFlow<List<News>>
        get() = _news

    private val _currentFilter = MutableStateFlow(false)
    val currentFilter: StateFlow<Boolean> = _currentFilter.asStateFlow()

    init {
        getListNews()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getListNews() {
        viewModelScope.launch {
            _currentFilter.flatMapLatest { showHidden ->
                repository.getNewsList(showHidden)
            }
                .collect { newsList ->
                    _news.value = newsList
                }
        }
    }

    fun toggleNewsVisibility() {
        _currentFilter.value = !_currentFilter.value
    }

    fun filterNews(news: List<News>, searchQuery: String): List<News> {
        return news.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
                    it.annotation.contains(searchQuery, ignoreCase = true)

        }
    }

    fun hideNews(news: News, stateHidden: Boolean) {
        viewModelScope.launch {
            news.hidden = !stateHidden
            repository.updateNews(news)
        }
    }
}