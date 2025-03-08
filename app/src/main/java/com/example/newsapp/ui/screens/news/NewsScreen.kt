package com.example.newsapp.ui.screens.news

import android.view.ViewGroup
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Close
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.newsapp.App
import com.example.newsapp.R
import com.example.newsapp.di.DaggerViewModelProvider
import com.example.newsapp.entity.News

@Composable
fun NewsScreen() {
    val newsViewModel = DaggerViewModelProvider.daggerViewModel {
        App.appComponent.newsViewModel()
    }
    val news by newsViewModel.news.observeAsState(emptyList())
    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchBar(onSearchQueryChanged = { searchQuery = it }, modifier = Modifier.weight(1f))
            RefreshNews { newsViewModel.getListNews() }
        }
        BoxNewsList(newsViewModel.filterNews(news, searchQuery), newsViewModel)
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
fun BoxNewsList(news: List<News>?, viewModel: NewsViewModel) {
    var selectedUrl by remember { mutableStateOf<String?>(null) }
    Box(modifier = Modifier.fillMaxSize()) {
        NewsList(
            news = news ?: emptyList(),
            onItemClick = { url -> selectedUrl = url },
            onHideNews = { news -> viewModel.hideNews(news) }
        )
        selectedUrl?.let { url ->
            WebViewOverlay(
                url = url,
                onClose = { selectedUrl = null }
            )
        }
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
        items(news) { newsItem ->
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

@Composable
fun WebViewOverlay(
    url: String,
    onClose: () -> Unit,
) {

    BackHandler(onBack = onClose)

    Box(
        modifier = Modifier
            .clickable(onClick = onClose)
            .padding(top = 10.dp)
    ) {
        WebViewScreen(
            url = url,
        )
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = Color.Black
            )
        }
    }
}

@Composable
fun WebViewScreen(url: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = {
            WebView(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        }, update = {
            it.loadUrl(url)
        })
    }
}