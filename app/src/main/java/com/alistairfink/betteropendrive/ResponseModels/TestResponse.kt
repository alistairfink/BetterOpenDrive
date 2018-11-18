package com.alistairfink.betteropendrive.responseodels

import com.google.gson.annotations.SerializedName

data class Test(
        @SerializedName("message") var message: String
)

data class Result(var testResponse: Test)