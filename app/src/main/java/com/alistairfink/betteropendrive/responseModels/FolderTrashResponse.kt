package com.alistairfink.betteropendrive.responseModels

import com.google.gson.annotations.SerializedName

data class FolderTrashResponse
(
        @SerializedName("DirUpdateTime") val DirUpdateTime: Long
)