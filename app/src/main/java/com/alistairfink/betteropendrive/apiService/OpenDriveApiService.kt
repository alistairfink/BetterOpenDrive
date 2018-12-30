package com.alistairfink.betteropendrive.apiService

import com.alistairfink.betteropendrive.Constants
import com.alistairfink.betteropendrive.requestModels.*
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
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor



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

    @POST("file/trash.json")
    fun trashFile(@Body body: FileTrashRequest)
            : Observable<FileTrashResponse>

    @POST("file/move_copy.json")
    fun moveCopyFile(@Body body: FileMoveCopyRequest)
            : Observable<FileMoveCopyResponse>

    @POST("folder/trash.json")
    fun trashFolder(@Body body: FolderTrashRequest)
            : Observable<FolderTrashResponse>

    companion object Factory
    {
        fun create() : OpenDriveApiService
        {
            // TODO : REMOVE LOGGING FOR PROD
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            var retrofit = Retrofit.Builder()
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(Constants.OpenDriveBaseURL)
                    .build()
            return retrofit.create(OpenDriveApiService::class.java)
        }
    }
}