package com.alistairfink.betteropendrive.requestModels

import com.alistairfink.betteropendrive.Constants
import com.google.gson.annotations.SerializedName

data class TestRequest(
    @SerializedName("apiKey") var ApiKey: String = Constants.ApiKey,
    @SerializedName("value") var Value: String
)