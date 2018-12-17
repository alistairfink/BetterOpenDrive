package com.alistairfink.betteropendrive.dataModels

import java.util.*

data class FolderModel
(
        var Name: String,
        var FolderId: String,
        var ParentFolderId: String?,
        var Folders: List<SubFolderModel>,
        var Files: List<FileModel>
)

data class SubFolderModel
(
        var FolderId: String,
        var Name: String,
        var Link: String,
        var DateCreated: Date,
        var DateModified: Date
)

data class FileModel
(
        var FileId: String,
        var Name: String,
        var Size: Int,
        var DateModified: Date,
        var FileHash: String,
        var Link: String
)