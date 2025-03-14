package com.example.newsapp.ui.screens.web

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebScreen(url: String, navController: NavController) {

    val webView = WebView(LocalContext.current).apply {
        webViewClient = WebViewClient()
        settings.javaScriptEnabled = true
        loadUrl(url)
    }

    DisposableEffect(Unit) {
        onDispose {
            webView.destroy()
        }
    }

    BackHandler {
        navController.popBackStack()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { webView },
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
        )

        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}