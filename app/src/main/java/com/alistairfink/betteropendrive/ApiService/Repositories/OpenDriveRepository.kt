package com.alistairfink.betteropendrive.apiService.repositories

import com.alistairfink.betteropendrive.apiService.OpenDriveApiService
import com.alistairfink.betteropendrive.requestModels.SessionLoginRequest
import com.alistairfink.betteropendrive.responseModels.SessionLoginResponse
import io.reactivex.Observable

object OpenDriveRepositoryProvider
{
    fun provideOpenDriveRepository() : OpenDriveRepository
    {
        return OpenDriveRepository(OpenDriveApiService.create());
    }
}

class OpenDriveRepository(var apiService: OpenDriveApiService)
{
    fun sessionLogin(body: SessionLoginRequest) : Observable<SessionLoginResponse>
    {
        return apiService.SessionLogin(body = body);
    }
}