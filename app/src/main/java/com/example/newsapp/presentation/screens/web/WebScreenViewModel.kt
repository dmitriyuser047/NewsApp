package com.example.newsapp.presentation.screens.web

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.net.URLDecoder
import javax.inject.Inject

@HiltViewModel
class WebScreenViewModel @Inject constructor() : ViewModel() {

    private var webView: WebView? by mutableStateOf(null)

    fun initWebView(url: String, webView: WebView) {
        webView.webViewClient = WebViewClient()
        webView.loadUrl(decodeUrl(url))
    }

    private fun decodeUrl(url: String): String {
        return URLDecoder.decode(url, "UTF-8")
    }

    override fun onCleared() {
        super.onCleared()
        webView?.destroy()
        webView = null
    }

}