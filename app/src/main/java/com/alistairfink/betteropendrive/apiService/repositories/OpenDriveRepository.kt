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
}