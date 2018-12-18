package com.alistairfink.betteropendrive.apiService.repositories

import com.alistairfink.betteropendrive.requestModels.SessionExistsRequest
import com.alistairfink.betteropendrive.apiService.OpenDriveApiService
import com.alistairfink.betteropendrive.requestModels.SessionLoginRequest
import com.alistairfink.betteropendrive.responseModels.FolderListResponse
import com.alistairfink.betteropendrive.responseModels.SessionExistsResponse
import com.alistairfink.betteropendrive.responseModels.SessionLoginResponse
import com.alistairfink.betteropendrive.responseModels.UsersInfoResponse
import io.reactivex.Observable

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
}