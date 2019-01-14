package com.alistairfink.betteropendrive.responseModels

import com.google.gson.annotations.SerializedName

data class FileCreateResponse
(
        @SerializedName("FileId") var FileId: String,
        @SerializedName("Name") var Name: String,
        @SerializedName("GroupID") var GroupId: String,
        @SerializedName("Extension") var Extension: String,
        @SerializedName("Size") var Size: String,
        @SerializedName("Views") var Views: String,
        @SerializedName("Version") var Version: String,
        @SerializedName("Downloads") var Downloads: String,
        @SerializedName("Access") var Access: String,
        @SerializedName("Link") var Link: String,
        @SerializedName("DownloadLink") var DownloadLink: String,
        @SerializedName("StreamingLink") var StreamingLink: String,
        @SerializedName("DirUpdateTime") var DirUpdateTime: Int,
        @SerializedName("TempLocation") var TempLocation: String,
        @SerializedName("SpeedLimit") var SpeedLimit: Int,
        @SerializedName("RequireCompression") var RequireCompression: Int,
        @SerializedName("RequireHash") var RequireHash: Int,
        @SerializedName("RequireHashOnly") var RequireHashOnly: Int
)