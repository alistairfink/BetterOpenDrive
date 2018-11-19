package com.alistairfink.betteropendrive.requestModels

import com.alistairfink.betteropendrive.Constants

data class TestRequest(
        var apiKey: String = Constants.ApiKey,
        var value: String
)