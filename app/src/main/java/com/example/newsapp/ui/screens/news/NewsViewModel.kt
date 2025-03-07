package com.example.newsapp.ui.screens.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.Repository
import com.omegar.libs.omegalaunchers.ActivityLauncher.Companion.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    init {
        getListNews()
    }

    private fun getListNews() {
        viewModelScope.launch {
            repository.testGetter()
        }
    }

}