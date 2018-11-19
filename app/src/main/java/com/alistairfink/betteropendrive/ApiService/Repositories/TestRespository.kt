package com.alistairfink.betteropendrive.ApiService.repositories

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

}