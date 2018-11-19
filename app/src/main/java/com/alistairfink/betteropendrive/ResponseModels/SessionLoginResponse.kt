package com.alistairfink.betteropendrive.responseModels

import com.google.gson.annotations.SerializedName

data class SessionLoginResponse(
    @SerializedName("SessionID") var SessionID: String,
    @SerializedName("UserName") var UserName: String,
    @SerializedName("UserFirstName") var UserFirstName: String,
    @SerializedName("UserLastName") var userLastName: String,
    @SerializedName("AccType") var AccType: Int,
    @SerializedName("UserLang") var UserLang: String,
    @SerializedName("UserID") var UserID: String,
    @SerializedName("IsAccountUser") var IsAccountUser: Boolean,
    @SerializedName("DriveName") var DriveName: String,
    @SerializedName("UserLevel") var UserLevel: Int,
    @SerializedName("UserPlan") var UserPlan: String,
    @SerializedName("FVersioning") var FVersioning: Int,
    @SerializedName("UserDomain") var UserDomain: String,
    @SerializedName("PartnerUsersDomain") var PartnerUsersDomain: String,
    @SerializedName("UploadSpeedLimit") var UploadSpeedLimit: Int,
    @SerializedName("DownloadSpeedLimit") var DownloadSpeedLimit: Int,
    @SerializedName("UploadsPerSecond") var UploadsPerSecond: Int,
    @SerializedName("DownloadsPerSecond") var DownloadsPerSecond: Int
)

data class SessionLoginResult(var response: SessionLoginResponse)