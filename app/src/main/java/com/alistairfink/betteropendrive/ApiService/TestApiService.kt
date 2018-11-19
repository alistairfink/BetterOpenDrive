package com.alistairfink.betteropendrive.apiService

import com.alistairfink.betteropendrive.Constants
import com.alistairfink.betteropendrive.requestModels.TestRequest
import com.alistairfink.betteropendrive.responseodels.Result
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

public interface TestApiService {
    @POST("Post")
    fun test(@Body body: TestRequest): Observable<Result>

    @GET("Get")
    fun test2() : Observable<Result>

    companion object Factory {
        fun create(): TestApiService {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://memes.alistairfink.com/test/")
                    .build()

            return retrofit.create(TestApiService::class.java);
        }
    }
}
