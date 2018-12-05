package com.alistairfink.betteropendrive.responseModels

import com.google.gson.annotations.SerializedName

data class SessionLoginResponse(
    @SerializedName("SessionID") val SessionID: String,
    @SerializedName("UserName") val UserName: String,
    @SerializedName("UserFirstName") val UserFirstName: String,
    @SerializedName("UserLastName") val userLastName: String,
    @SerializedName("AccType") val AccType: Int,
    @SerializedName("UserLang") val UserLang: String,
    @SerializedName("UserID") val UserID: String,
    @SerializedName("IsAccountUser") val IsAccountUser: Boolean,
    @SerializedName("DriveName") val DriveName: String,
    @SerializedName("UserLevel") val UserLevel: Int,
    @SerializedName("UserPlan") val UserPlan: String,
    @SerializedName("FVersioning") val FVersioning: Int,
    @SerializedName("UserDomain") val UserDomain: String,
    @SerializedName("PartnerUsersDomain") val PartnerUsersDomain: String,
    @SerializedName("UploadSpeedLimit") val UploadSpeedLimit: Int,
    @SerializedName("DownloadSpeedLimit") val DownloadSpeedLimit: Int,
    @SerializedName("UploadsPerSecond") val UploadsPerSecond: Int,
    @SerializedName("DownloadsPerSecond") val DownloadsPerSecond: Int
)

data class SessionLoginResult(val response: SessionLoginResponse)