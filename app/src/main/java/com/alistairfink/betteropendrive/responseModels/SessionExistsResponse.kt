package com.alistairfink.betteropendrive.responseModels

import com.google.gson.annotations.SerializedName

data class SessionExistsResponse(
    @SerializedName("result") val Result: Boolean
)