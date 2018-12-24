package com.alistairfink.betteropendrive

import android.support.v7.app.AppCompatActivity
import android.webkit.WebView
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.file_preview.*
import kotlinx.android.synthetic.main.file_preview.view.*
import android.content.Intent
import android.view.MotionEvent




class FilePreview: Fragment()
{
    companion object
    {
        fun newInstance(folderId: String): FilePreview
        {
            var fragment = FilePreview()
            var args = Bundle()
            args.putString("file", folderId)
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanteState: Bundle?): View
    {
        return inflater.inflate(R.layout.file_preview, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        var file = arguments.getString("file")

        var path = "file://$file"
        file_preview_webview.settings.javaScriptEnabled = true
        file_preview_webview.settings.loadWithOverviewMode = true
        file_preview_webview.settings.useWideViewPort = true
        file_preview_webview.settings.displayZoomControls = true
        file_preview_webview.settings.builtInZoomControls = true
        file_preview_webview.loadUrl(path)

        /*file_preview_webview.setOnTouchListener { v, event ->
            // toggleControls()
            true
        }*/
    }

    fun toggleControls()
    {
        if (file_preview_webview.settings.displayZoomControls)
        {
            file_preview_webview.settings.displayZoomControls = false
            return
        }

        file_preview_webview.settings.displayZoomControls = true
    }
}