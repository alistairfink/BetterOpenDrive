package com.alistairfink.betteropendrive.helpers

import android.content.Context
import com.alistairfink.betteropendrive.dataModels.FolderModel
import com.google.gson.Gson
import java.io.File

class InternalStorageClient(context: Context) {
    private val _context = context;

    private fun write(fileContents: String, fileName: String)
    {
        _context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(fileContents.toByteArray())
        }
    }

    private fun read(fileName: String): String?
    {
        var directory = _context.filesDir
        var file = File(directory, fileName)
        if (file.exists())
        {
            return file.readText()
        }

        return null
    }

    fun writeFolder(folder: FolderModel, fileName: String)
    {
        var gson = Gson()
        var folderJson = gson.toJson(folder)
        write(folderJson, fileName)
    }

    fun readFolder(fileName: String): FolderModel?
    {
        var gson = Gson()
        var json = read(fileName)
        if (json.isNullOrEmpty()) {
            return null
        }

        return gson.fromJson<FolderModel>(json, FolderModel::class.java)
    }
}