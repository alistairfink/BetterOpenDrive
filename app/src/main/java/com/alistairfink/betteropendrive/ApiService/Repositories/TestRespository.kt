package com.alistairfink.betteropendrive.apiService.repositories

import com.alistairfink.betteropendrive.apiService.TestApiService
import com.alistairfink.betteropendrive.requestModels.TestRequest
import com.alistairfink.betteropendrive.responseodels.Result

object TestRepositoryProvider {
    fun provideTesthRepository(): TestRepository {
        return TestRepository(TestApiService.Factory.create())
    }

}

class TestRepository(val apiService: TestApiService) {

    fun test(body: TestRequest): io.reactivex.Observable<Result> {
        return apiService.test(body = body);
    }

    fun test2(): io.reactivex.Observable<Result> {
        return apiService.test2();
    }

}