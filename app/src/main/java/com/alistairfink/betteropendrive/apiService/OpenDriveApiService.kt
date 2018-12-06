package com.alistairfink.betteropendrive.apiService

import com.alistairfink.betteropendrive.Constants
import com.alistairfink.betteropendrive.requestModels.SessionExistsRequest
import com.alistairfink.betteropendrive.requestModels.SessionLoginRequest
import com.alistairfink.betteropendrive.responseModels.SessionExistsResponse
import com.alistairfink.betteropendrive.responseModels.SessionLoginResponse
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface OpenDriveApiService
{
    @POST("session/login.json")
    fun sessionLogin(@Body body: SessionLoginRequest)
            : Observable<SessionLoginResponse>

    @POST("session/exists.json")
    fun sessionExists(@Body body: SessionExistsRequest)
            : Observable<SessionExistsResponse>

    companion object Factory
    {
        fun create() : OpenDriveApiService
        {
            var retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(Constants.OpenDriveBaseURL)
                    .build();
            return retrofit.create(OpenDriveApiService::class.java);
        }
    }
}