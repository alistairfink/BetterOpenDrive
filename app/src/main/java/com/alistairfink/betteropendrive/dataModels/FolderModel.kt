package com.alistairfink.betteropendrive.dataModels

import android.util.Base64
import com.alistairfink.betteropendrive.SharedPreferenceConstants
import com.alistairfink.betteropendrive.apiService.repositories.MiscellaneousRepositoryProvider
import com.alistairfink.betteropendrive.helpers.SharedPreferencesClient
import com.alistairfink.betteropendrive.responseModels.FolderListResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
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
                var thumbLink = file.ThumbLink
                // TODO : Figure out a way to either do all of this async or get thumbnail synchronously and encode it
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

            var folderModel = FolderModel (
                    Name = name,
                    ParentFolderId = parentFolderId,
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
        var Link: String,
        var Thumbnail: String
)