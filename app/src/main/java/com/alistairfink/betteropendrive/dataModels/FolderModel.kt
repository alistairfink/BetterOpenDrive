package com.alistairfink.betteropendrive.dataModels

import java.util.*

data class FolderModel
(
        val Name: String,
        val FolderId: String,
        val ParentFolderId: String?,
        val Folders: List<SubFolderModel>,
        val Files: List<FileModel>
)

data class SubFolderModel
(
        val FolderId: String,
        val Name: String,
        val Link: String,
        val DateCreated: Date,
        val DateModified: Date
)

data class FileModel
(
        val FileId: String,
        val Name: String,
        val Size: Int,
        val DateModified: Date,
        val FileHash: String,
        val Link: String
)