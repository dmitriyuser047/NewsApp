package com.example.newsapp.ui.screens.news

import androidx.compose.runtime.Composable
import com.example.newsapp.App
import com.example.newsapp.di.DaggerViewModelProvider
import javax.inject.Inject

@Composable
fun NewsScreen() {
    val newsViewModel = DaggerViewModelProvider.daggerViewModel {
        App.appComponent.newsViewModel()
    }
}