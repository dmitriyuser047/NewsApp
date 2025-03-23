package com.example.newsapp.ui.screens.news

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.newsapp.R
import com.example.newsapp.entity.News
import com.example.newsapp.route.NavigationItem

@Composable
fun NewsScreen(navController: NavController, viewModel: NewsViewModel = hiltViewModel()) {
    val news by viewModel.news.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }
    val currentFilterHidden by viewModel.currentFilter.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchBar(
                onSearchQueryChanged = { searchQuery = it },
                modifier = Modifier.weight(1f)
            )
            ToggleHiddenNewsButton(
                showHidden = currentFilterHidden,
                onToggle = viewModel::toggleNewsVisibility,
                modifier = Modifier.padding(start = 8.dp, top = 50.dp)
            )
//            RefreshNews { viewModel.getListNews() }
        }
        val filteredNews =
            remember(news, searchQuery) { viewModel.filterNews(news, searchQuery) }
        NewsList(
            news = filteredNews,
            onItemClick = { url: String -> navController.navigate(NavigationItem.Web(url)) },
            onHideNews = { news: News -> viewModel.hideNews(news, currentFilterHidden) }
        )
    }
}

@Composable
fun ToggleHiddenNewsButton(
    showHidden: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier
) {
    IconButton(
        onClick = onToggle,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (showHidden) Icons.Rounded.FavoriteBorder else Icons.Rounded.Favorite,
            contentDescription = if (showHidden) "Show normal news" else "Show hidden news",
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    onSearchQueryChanged: (String) -> Unit,
    modifier: Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    androidx.compose.material3.SearchBar(
        query = searchQuery,
        onQueryChange = {
            searchQuery = it
            onSearchQueryChanged(it)
            active = true
        },
        onSearch = { active = false },
        active = false,
        onActiveChange = { active = it },

        modifier = modifier,

        placeholder = { Text("Search") },

        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = null,
            )
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    searchQuery = ""
                    active = false
                    onSearchQueryChanged("")
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Clear search",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surface,
            dividerColor = Color.Transparent
        ),
        tonalElevation = 0.dp,
        windowInsets = SearchBarDefaults.windowInsets,
        shape = MaterialTheme.shapes.medium
    ) {}
}

@Composable
fun RefreshNews(
    onRefresh: () -> Unit
) {
    IconButton(
        onClick = onRefresh,
        modifier = Modifier.padding(top = 30.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.Refresh,
            contentDescription = "Refresh",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun NewsList(
    news: List<News>,
    onItemClick: (String) -> Unit,
    onHideNews: (News) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp, bottom = 35.dp)
    ) {
        items(
            items = news,
            key = { it.id }
        ) { newsItem ->
            NewsView(
                news = newsItem,
                clickItemNews = { onItemClick(newsItem.mobileUrl) },
                onHideNews = { onHideNews(newsItem) }
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NewsView(
    news: News,
    clickItemNews: (String) -> Unit,
    onHideNews: (News) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clickable { clickItemNews(news.mobileUrl) },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = news.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = { onHideNews(news) }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Clear,
                    contentDescription = "Hide news",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        Text(
            text = news.annotation,
            modifier = Modifier.padding(8.dp)
        )
        GlideImage(
            model = news.img,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop
        ) { requestBuilder ->
            requestBuilder.placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.page_not_found)
        }
    }
}