package com.alistairfink.betteropendrive.requestModels

import com.google.gson.annotations.SerializedName

data class SessionLoginRequest
(
        @SerializedName("username") var UserName: String,
        @SerializedName("passwd") var Password: String
)