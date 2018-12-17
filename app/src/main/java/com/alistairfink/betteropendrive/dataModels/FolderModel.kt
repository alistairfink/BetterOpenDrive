package com.alistairfink.betteropendrive.dataModels

import com.alistairfink.betteropendrive.responseModels.FolderListResponse
import java.util.*

class FolderModelHelper
{
    companion object
    {
        fun FolderModelHelper(folderListResponse: FolderListResponse): FolderModel
        {
            var subFolders: MutableList<SubFolderModel> = mutableListOf()
            for (subFolder in folderListResponse.Folders)
            {
                var folder = SubFolderModel(
                        FolderId = subFolder.FolderId,
                        Name = subFolder.Name,
                        Link = subFolder.Link,
                        DateCreated = Date(subFolder.DateCreated),
                        DateModified = Date(subFolder.DateModified)
                )
                subFolders.add(folder)
            }

            var files: MutableList<FileModel> = mutableListOf()
            for (file in folderListResponse.Files)
            {
                // TODO : get thumbnail and save as base64
                var fileModel = FileModel(
                        FileId = file.FileId,
                        Name = file.Name,
                        Size = file.Size,
                        DateModified =  Date(file.DateModified),
                        FileHash = file.FileHash,
                        Link = file.DownloadLink
                )
                files.add(fileModel)
            }

            var folderModel = FolderModel (
                    Name = folderListResponse.Name,
                    ParentFolderId = folderListResponse.ParentFolderId,
                    Folders = subFolders,
                    Files = files
            )
            return folderModel
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
        var Link: String
)