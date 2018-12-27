package com.alistairfink.betteropendrive.helpers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.RelativeLayout

class CustomWebView(private val context: Context, private val webView: WebView, private val background: LinearLayout): WebViewClient()
{
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?)
    {
        background.visibility = RelativeLayout.VISIBLE
        webView.setBackgroundColor(Color.TRANSPARENT)
        super.onPageStarted(view, url, favicon)
    }
    override fun onPageFinished(view: WebView, url: String)
    {
        webView.setBackgroundColor(Color.WHITE)
        background.visibility = RelativeLayout.INVISIBLE
        super.onPageFinished(view, url)
    }
}