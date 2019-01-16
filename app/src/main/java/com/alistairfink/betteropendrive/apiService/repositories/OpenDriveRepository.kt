package com.alistairfink.betteropendrive.apiService.repositories

import com.alistairfink.betteropendrive.apiService.OpenDriveApiService
import com.alistairfink.betteropendrive.requestModels.*
import com.alistairfink.betteropendrive.responseModels.*
import io.reactivex.Observable
import io.reactivex.Observer
import okhttp3.ResponseBody

object OpenDriveRepositoryProvider
{
    fun provideOpenDriveRepository() : OpenDriveRepository
    {
        return OpenDriveRepository(OpenDriveApiService.create())
    }
}

class OpenDriveRepository(var apiService: OpenDriveApiService)
{
    fun sessionLogin(body: SessionLoginRequest)
            : Observable<SessionLoginResponse>
    {
        return apiService.sessionLogin(body = body)
    }

    fun sessionExists(body: SessionExistsRequest)
            : Observable<SessionExistsResponse>
    {
        return apiService.sessionExists(body = body)
    }

    fun usersInfo(sessionId: String)
            : Observable<UsersInfoResponse>
    {
        return apiService.usersInfo(SessionId = sessionId)
    }

    fun folderList(sessionId: String, folderId: String)
            : Observable<FolderListResponse>
    {
        return apiService.folderList(SessionId = sessionId, FolderId = folderId)
    }

    fun fileRename(body: FileRenameRequest)
            : Observable<FileRenameResponse>
    {
        return apiService.fileRename(body = body)
    }

    fun downloadFile(fileId: String)
            : Observable<ResponseBody>
    {
        return apiService.downloadFile(FileId = fileId)
    }

    fun trashFile(body: FileTrashRequest)
            : Observable<FileTrashResponse>
    {
        return apiService.trashFile(body = body)
    }

    fun moveCopyFile(body: FileMoveCopyRequest)
            : Observable<FileMoveCopyResponse>
    {
        return apiService.moveCopyFile(body = body)
    }

    fun trashFolder(body: FolderTrashRequest)
            : Observable<FolderTrashResponse>
    {
        return apiService.trashFolder(body = body)
    }

    fun renameFolder(body: FolderRenameRequest)
            : Observable<FolderRenameResponse>
    {
        return apiService.renameFolder(body = body)
    }

    fun moveCopyFolder(body: FolderMoveCopyRequest)
            : Observable<FolderMoveCopyResponse>
    {
        return apiService.moveCopyFolder(body = body)
    }

    fun folderCreate(body: FolderCreateRequest)
            : Observable<FolderCreateResponse>
    {
        return apiService.folderCreate(body = body)
    }

    fun fileCreate(body: FileCreateRequest)
            : Observable<FileCreateResponse>
    {
        return apiService.fileCreate(body = body)
    }

    fun fileUploadClose(body: FileUploadCloseRequest)
            : Observable<FileUploadCloseResponse>
    {
        return apiService.fileUploadClose(body = body)
    }

    fun fileUpload(body: FileUploadRequest)
            : Observable<Int>
    {
        return apiService.fileUpload(
                SessionId = body.SessionId,
                FileId = body.FileId,
                TempLocation = body.TempLocation,
                ChunkOffset = body.ChunkOffset,
                ChunkSize = body.ChunkSize,
                FileData = body.FileData
        )
    }
}