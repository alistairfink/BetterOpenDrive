package com.alistairfink.betteropendrive.apiService

import com.alistairfink.betteropendrive.Constants
import com.alistairfink.betteropendrive.requestModels.SessionExistsRequest
import com.alistairfink.betteropendrive.requestModels.SessionLoginRequest
import com.alistairfink.betteropendrive.responseModels.SessionExistsResponse
import com.alistairfink.betteropendrive.responseModels.SessionLoginResponse
import com.alistairfink.betteropendrive.responseModels.UsersInfoResponse
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface MiscellaneousApiService
{
    @GET
    fun getImage(@Url URL: String)
            : Observable<ResponseBody>

    companion object Factory
    {
        fun create() : MiscellaneousApiService
        {
            var retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://localhost:8080")
                    .build()
            return retrofit.create(MiscellaneousApiService::class.java)
        }
    }
}