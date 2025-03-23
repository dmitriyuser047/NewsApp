package com.example.newsapp.route

import kotlinx.serialization.Serializable

sealed class NavigationItem {

    @Serializable
    data object News : NavigationItem()

    @Serializable
    data class Web(val url: String) : NavigationItem()
}