package com.alistairfink.betteropendrive.responseModels

import com.google.gson.annotations.SerializedName

data class FileTrashResponse
(
        @SerializedName("DirUpdateTime") val DirUpdateTime: Long
)