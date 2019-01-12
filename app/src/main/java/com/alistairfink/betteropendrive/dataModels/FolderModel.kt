package com.alistairfink.betteropendrive.dataModels

import com.alistairfink.betteropendrive.responseModels.Files
import com.alistairfink.betteropendrive.responseModels.FolderListResponse
import com.alistairfink.betteropendrive.responseModels.Folders
import com.google.gson.annotations.SerializedName
import java.io.File
import java.util.*

class FolderModelHelper
{
    companion object
    {
        fun toFolderModel(folderListResponse: FolderListResponse, isRoot: Boolean = false): FolderModel
        {
            var subFolders: MutableList<SubFolderModel> = mutableListOf()
            for (subFolder in folderListResponse.Folders)
            {
                subFolders.add(toSubFolderModel(subFolder))
            }

            var files: MutableList<FileModel> = mutableListOf()
            for (file in folderListResponse.Files)
            {
                files.add(toFileModel(file))
            }

            var parentFolderId = folderListResponse.ParentFolderId
            var name = folderListResponse.Name
            if (isRoot)
            {
                parentFolderId = "0"
                name = "All Files"
            }

            return FolderModel (
                    Name = name,
                    ParentFolderId = parentFolderId,
                    Folders = subFolders,
                    Files = files
            )
        }
        fun toSubFolderModel(subFolder: Folders): SubFolderModel
        {
             return SubFolderModel(
                    FolderId = subFolder.FolderId,
                    Name = subFolder.Name,
                    Link = subFolder.Link,
                     ChildFolders = subFolder.ChildFolders,
                    DateCreated = Date(subFolder.DateCreated*1000L),
                    DateModified = Date(subFolder.DateModified*1000L)
            )
        }
        fun toFileModel(file: Files): FileModel
        {
            return FileModel(
                    FileId = file.FileId,
                    Name = file.Name,
                    Size = file.Size,
                    DateModified =  Date(file.DateModified*1000L),
                    FileHash = file.FileHash,
                    Link = file.DownloadLink,
                    Thumbnail = file.ThumbLink,
                    Extension = file.Extension,
                    Version = file.Version
            )
        }
    }
}

data class FolderModel
(
        var Name: String,
        var ParentFolderId: String?,
        var Folders: MutableList<SubFolderModel>,
        var Files: MutableList<FileModel>
)

data class SubFolderModel
(
        var FolderId: String,
        var Name: String,
        var Link: String,
        var ChildFolders: Int,
        var DateCreated: Date,
        var DateModified: Date
)

data class FileModel
(
        var FileId: String,
        var Name: String,
        var Size: Int,
        var Version: String,
        var DateModified: Date,
        var FileHash: String,
        var Link: String,
        var Thumbnail: String,
        var Extension: String
)