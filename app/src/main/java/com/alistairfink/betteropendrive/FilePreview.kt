package com.alistairfink.betteropendrive

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import kotlinx.android.synthetic.main.file_preview.*
import kotlinx.android.synthetic.main.file_preview.view.*
import java.io.File


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
        // TODO : Add Default "Not Supported" View - HARD
        // TODO : Add File Options
        super.onViewCreated(view, savedInstanceState)
        var file = arguments.getString("file")

        var path: String
        if (file.substring(file.lastIndex-3) == ".pdf")
        {
            var actualFile = File(file)
            var absolPath = actualFile.absolutePath
            path = "file:///android_asset/pdfjs/web/viewer.html?file=file://${actualFile.absolutePath}#zoom=page-width"
        }
        else
        {
            path = "file://$file"
        }
        //file_preview_webview.webViewClient = test(this.context)
        file_preview_webview.settings.javaScriptEnabled = true
        file_preview_webview.settings.loadWithOverviewMode = true
        file_preview_webview.settings.useWideViewPort = true
        file_preview_webview.settings.displayZoomControls = true
        file_preview_webview.settings.builtInZoomControls = true
        file_preview_webview.settings.allowFileAccessFromFileURLs = true
        file_preview_webview.settings.allowFileAccess = true
        file_preview_webview.loadUrl(path)
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

    override fun onDestroy()
    {
        var filePath = arguments.getString("file")
        var file = File(filePath)
        file.delete()
        super.onDestroy()
    }
}