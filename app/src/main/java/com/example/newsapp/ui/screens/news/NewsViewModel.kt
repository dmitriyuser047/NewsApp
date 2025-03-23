package com.example.newsapp.ui.screens.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.Repository
import com.example.newsapp.entity.News
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val news: StateFlow<List<News>>

    private val _currentFilter = MutableStateFlow(false)
    val currentFilter: StateFlow<Boolean> = _currentFilter.asStateFlow()

    init {
        news = _currentFilter
            .flatMapLatest { showHidden -> repository.getNewsList(showHidden) }
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
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
            val newNews = news.copy(hidden = !stateHidden)
            repository.updateNews(newNews)
        }
    }
}