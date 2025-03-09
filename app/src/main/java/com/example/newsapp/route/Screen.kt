package com.example.newsapp.route

import java.net.URLEncoder

enum class Screen {
    NEWS
}

sealed class NavigationItem(val route: String) {
    data object News : NavigationItem(Screen.NEWS.name)

    data object Web : NavigationItem("web/{url}") {
        fun createRoute(url: String) = "web/${URLEncoder.encode(url, "UTF-8")}"
    }
}