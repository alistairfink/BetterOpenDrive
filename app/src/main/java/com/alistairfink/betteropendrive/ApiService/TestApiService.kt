package com.alistairfink.betteropendrive.apiService

import com.alistairfink.betteropendrive.Constants
import com.alistairfink.betteropendrive.responseodels.Result
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST
import retrofit2.http.Query

public interface TestApiService {
    @POST("Post")
    fun test(@Query("apiKey") apiKey: String = Constants.ApiKey,
               @Query("value") value: String): Observable<Result>

    companion object Factory {
        fun create(): TestApiService {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://localhost:8080/")
                    .build()

            return retrofit.create(TestApiService::class.java);
        }
    }
}
