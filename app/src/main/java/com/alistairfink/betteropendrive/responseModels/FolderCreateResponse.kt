package com.alistairfink.betteropendrive.responseModels

import com.google.gson.annotations.SerializedName

data class FolderCreateResponse
(
        @SerializedName("FolderID") var FolderId: String,
        @SerializedName("Name") var Name: String,
        @SerializedName("DateCreated") var DateCreated: Integer,
        @SerializedName("DirUpdateTime") var DirUpdateTime: Int,
        @SerializedName("Access") var Access: Integer,
        @SerializedName("DateModified") var DateModified: Int,
        @SerializedName("Shared") var Shared: String,
        @SerializedName("Description") var Description: String,
        @SerializedName("Link") var Link: String
)