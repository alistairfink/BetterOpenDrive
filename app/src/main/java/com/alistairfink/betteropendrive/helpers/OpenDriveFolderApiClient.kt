package com.alistairfink.betteropendrive.helpers

import android.content.Context
import com.alistairfink.betteropendrive.InternalStroageConstants
import com.alistairfink.betteropendrive.SharedPreferenceConstants
import com.alistairfink.betteropendrive.apiService.repositories.OpenDriveRepositoryProvider
import com.alistairfink.betteropendrive.dataModels.FolderModel
import com.alistairfink.betteropendrive.dataModels.FolderModelHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class OpenDriveFolderApiClient(private val context: Context)
{
    private val compositeDisposable = CompositeDisposable()

    fun cut()
    {

    }

    fun copy()
    {

    }

    fun rename()
    {

    }

    fun trash()
    {

    }

    fun getFolder(folderId: String, callBack: (FolderModel) -> Unit)
    {
        var sharedPreferences = SharedPreferencesClient(context)
        var sessionId = sharedPreferences.getString(SharedPreferenceConstants.SessionId) as String
        var repository = OpenDriveRepositoryProvider.provideOpenDriveRepository()
        compositeDisposable.add(
                repository.folderList(sessionId, folderId)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            var resultData =
                                    if (folderId == "0")
                                    {
                                        FolderModelHelper.toFolderModel(result, true)
                                    }
                                    else
                                    {
                                        FolderModelHelper.toFolderModel(result)
                                    }

                            var internalStorage = InternalStorageClient(this.context)
                            internalStorage.writeFolder(resultData, InternalStroageConstants.FolderPrefix + folderId)
                            callBack.invoke(resultData)
                        }, { error ->
                            error.printStackTrace()
                        })
        )
    }
}