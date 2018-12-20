package com.alistairfink.betteropendrive.dataModels

import com.alistairfink.betteropendrive.responseModels.FolderListResponse
import java.util.*

class FolderModelHelper
{
    companion object
    {
        fun toDataModel(folderListResponse: FolderListResponse, isRoot: Boolean = false): FolderModel
        {
            var subFolders: MutableList<SubFolderModel> = mutableListOf()
            for (subFolder in folderListResponse.Folders)
            {
                var folder = SubFolderModel(
                        FolderId = subFolder.FolderId,
                        Name = subFolder.Name,
                        Link = subFolder.Link,
                        DateCreated = Date(subFolder.DateCreated*1000L),
                        DateModified = Date(subFolder.DateModified*1000L)
                )
                subFolders.add(folder)
            }

            var files: MutableList<FileModel> = mutableListOf()
            for (file in folderListResponse.Files)
            {
                var fileModel = FileModel(
                        FileId = file.FileId,
                        Name = file.Name,
                        Size = file.Size,
                        DateModified =  Date(file.DateModified*1000L),
                        FileHash = file.FileHash,
                        Link = file.DownloadLink,
                        Thumbnail = file.ThumbLink
                )
                files.add(fileModel)
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
        var Link: String,
        var Thumbnail: String
)