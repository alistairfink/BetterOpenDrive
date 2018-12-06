package com.alistairfink.betteropendrive.requestModels

import com.google.gson.annotations.SerializedName

data class SessionExistsRequest
(
        @SerializedName("session_id") var SessionId: String
)