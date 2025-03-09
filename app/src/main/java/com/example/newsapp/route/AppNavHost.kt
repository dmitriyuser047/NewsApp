package com.example.newsapp.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.newsapp.ui.screens.news.NewsScreen
import com.example.newsapp.ui.screens.web.WebScreen
import java.net.URLDecoder

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavigationItem.News.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavigationItem.News.route) {
            NewsScreen(navController)
        }
        composable(
            route = NavigationItem.Web.route,
            arguments = listOf(navArgument("url") {
                type = NavType.StringType
                nullable = true
            })
        ) { backStackEntry ->
            val encodedUrl = backStackEntry.arguments?.getString("url")
            if (encodedUrl != null) {
                val decodedUrl = URLDecoder.decode(encodedUrl, "UTF-8")
                WebScreen(decodedUrl, navController)
            }
        }
    }
}