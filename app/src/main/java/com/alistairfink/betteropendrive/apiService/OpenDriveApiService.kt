package com.alistairfink.betteropendrive.apiService

import com.alistairfink.betteropendrive.Constants
import com.alistairfink.betteropendrive.requestModels.FileRenameRequest
import com.alistairfink.betteropendrive.requestModels.SessionExistsRequest
import com.alistairfink.betteropendrive.requestModels.SessionLoginRequest
import com.alistairfink.betteropendrive.responseModels.*
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface OpenDriveApiService
{
    @POST("session/login.json")
    fun sessionLogin(@Body body: SessionLoginRequest)
            : Observable<SessionLoginResponse>

    @POST("session/exists.json")
    fun sessionExists(@Body body: SessionExistsRequest)
            : Observable<SessionExistsResponse>

    @GET("users/info.json/{sessionId}")
    fun usersInfo(@Path("sessionId") SessionId: String)
            : Observable<UsersInfoResponse>

    @GET("folder/list.json/{sessionId}/{folderId}")
    fun folderList(@Path("sessionId") SessionId: String, @Path("folderId") FolderId: String)
            : Observable<FolderListResponse>

    @POST("file/rename.json")
    fun fileRename(@Body body: FileRenameRequest)
            : Observable<FileRenameResponse>

    @GET("download/file.json/{fileId}")
    fun downloadFile(@Path("fileId") FileId: String)
            : Observable<ResponseBody>

    companion object Factory
    {
        fun create() : OpenDriveApiService
        {
            var retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(Constants.OpenDriveBaseURL)
                    .build()
            return retrofit.create(OpenDriveApiService::class.java)
        }
    }
}