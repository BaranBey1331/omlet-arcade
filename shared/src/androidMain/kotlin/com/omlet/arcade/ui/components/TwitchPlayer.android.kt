package com.omlet.arcade.ui.components

import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
actual fun TwitchPlayer(channel: String, modifier: Modifier) {
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.mediaPlaybackRequiresUserGesture = false
                webViewClient = WebViewClient()
                webChromeClient = WebChromeClient()
                val embedUrl = "https://player.twitch.tv/?channel=$channel&parent=localhost&autoplay=true"
                loadUrl(embedUrl)
            }
        },
        update = { webView ->
            val embedUrl = "https://player.twitch.tv/?channel=$channel&parent=localhost&autoplay=true"
            webView.loadUrl(embedUrl)
        }
    )
}
