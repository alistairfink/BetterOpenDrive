package com.alistairfink.betteropendrive.apiService.repositories

import com.alistairfink.betteropendrive.apiService.MiscellaneousApiService
import io.reactivex.Observable
import okhttp3.ResponseBody

object MiscellaneousRepositoryProvider
{
    fun provideMiscellaneousApiServiceRepository() : MiscellaneousRepository
    {
        return MiscellaneousRepository(MiscellaneousApiService.create())
    }
}

class MiscellaneousRepository(var apiService: MiscellaneousApiService)
{
    fun getImage(urlPath: String)
            : Observable<ResponseBody>
    {
        return apiService.getImage(URL = urlPath)
    }
}