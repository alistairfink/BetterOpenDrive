package com.alistairfink.betteropendrive.apiService

import com.alistairfink.betteropendrive.Constants
import com.alistairfink.betteropendrive.requestModels.SessionLoginRequest
import com.alistairfink.betteropendrive.responseModels.SessionLoginResult
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

public interface OpenDriveApiService
{
    @POST("/session/login.json")
    fun SessionLogin(@Body body: SessionLoginRequest)
            : Observable<SessionLoginResult>

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