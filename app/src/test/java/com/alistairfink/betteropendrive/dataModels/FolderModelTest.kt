package com.alistairfink.betteropendrive.dataModels

import com.alistairfink.betteropendrive.responseModels.Files
import com.alistairfink.betteropendrive.responseModels.FolderListResponse
import com.alistairfink.betteropendrive.responseModels.Folders
import junit.framework.Assert
import org.junit.Test
import java.util.*

class FolderModelTest {
    companion object
    {
        fun folderListResponseFactory(): FolderListResponse
        {
            return FolderListResponse(
                    DirUpdateTime = 1,
                    Name = "FolderFactory",
                    ParentFolderId = "123123",
                    DirectFolderLink = "https://www.google.com",
                    ResponseType = 1,
                    Folders = mutableListOf(),
                    Files = mutableListOf()
            )
        }
        fun foldersFactory(): Folders
        {
            return Folders(
                    FolderId = "321321" ,
                    Name = "SubFolder",
                    DateCreated = 1,
                    DirUpdateTime = 1,
                    Access = 2,
                    DateModified = 1,
                    Shared = false,
                    ChildFolders = 1,
                    Link = "http://www.alistairfink.com",
                    Encrypted = 1
            )
        }
        fun filesFactory(): Files
        {
            return Files(
                    FileId = "666",
                    Name = "FileFactory",
                    GroupId = 1,
                    Extension = ".pdf",
                    Size = 123,
                    Views = 1,
                    Version = "1",
                    Downloads = 1,
                    DateModified = 1,
                    Access = 1,
                    FileHash = "123",
                    Link = "https://www.google.ca",
                    DownloadLink = "https://www.google.ca",
                    StreamingLink = "https://www.google.ca",
                    TempStreamingLink = "https://www.google.ca",
                    ThumbLink = "https://www.google.ca",
                    Password = "123",
                    EditOnline = 1
            )
        }
    }

    @Test
    fun toFolderModelTest()
    {
        var folder = folderListResponseFactory()
        folder.Folders.add(foldersFactory())
        folder.Folders.add(foldersFactory())
        folder.Files.add(filesFactory())
        folder.Files.add(filesFactory())

        var folderModel = FolderModelHelper.toFolderModel(folder)

        Assert.assertEquals(folder.Name, folderModel.Name)
        Assert.assertEquals(folder.ParentFolderId, folderModel.ParentFolderId)
        Assert.assertEquals(folder.Folders.size, folderModel.Folders.size)
        Assert.assertEquals(folder.Files.size, folderModel.Files.size)
    }

    @Test
    fun toSubFolderModelTest()
    {
        var subFolder = foldersFactory()
        var subFolderModel = FolderModelHelper.toSubFolderModel(subFolder)

        Assert.assertEquals(subFolder.FolderId, subFolderModel.FolderId)
        Assert.assertEquals(subFolder.Name, subFolderModel.Name)
        Assert.assertEquals(subFolder.Link, subFolderModel.Link)
        Assert.assertEquals(Date(subFolder.DateCreated*1000L), subFolderModel.DateCreated)
        Assert.assertEquals(Date(subFolder.DateModified*1000L), subFolderModel.DateModified)
    }

    @Test
    fun toFileModelTest()
    {
        var file = filesFactory()
        var fileModel = FolderModelHelper.toFileModel(file)

        Assert.assertEquals(file.FileId, fileModel.FileId)
        Assert.assertEquals(file.Name, fileModel.Name)
        Assert.assertEquals(file.Size, fileModel.Size)
        Assert.assertEquals(Date(file.DateModified*1000L), fileModel.DateModified)
        Assert.assertEquals(file.FileHash, fileModel.FileHash)
        Assert.assertEquals(file.DownloadLink, fileModel.Link)
        Assert.assertEquals(file.ThumbLink, fileModel.Thumbnail)
    }
}