package com.example.newsapp.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newsapp.ui.screens.news.NewsScreen
import com.example.newsapp.ui.screens.web.WebScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: NavigationItem = NavigationItem.News
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<NavigationItem.News> {
            NewsScreen(navController)
        }

        composable<NavigationItem.Web> { backStackEntry ->
            val url = backStackEntry.arguments?.getString("url")
            url?.let { WebScreen(it, navController) }
        }
    }
}