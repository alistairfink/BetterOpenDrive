package com.alistairfink.betteropendrive.helpers

import android.content.Context
import com.alistairfink.betteropendrive.SharedPreferenceConstants
import com.alistairfink.betteropendrive.apiService.repositories.OpenDriveRepositoryProvider
import com.alistairfink.betteropendrive.dataModels.FileModel
import com.alistairfink.betteropendrive.requestModels.FileRenameRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class OpenDriveFileApiClient(private val context: Context)
{
    private val compositeDisposable = CompositeDisposable()

    fun download(file: FileModel, callBack: (FileModel, ByteArray) -> Unit)
    {
        var repository = OpenDriveRepositoryProvider.provideOpenDriveRepository()
        compositeDisposable.add(
                repository.downloadFile(file.FileId)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            callBack.invoke(file, result.bytes())
                        }, { error ->
                            android.os.Debug.waitForDebugger()
                            error.printStackTrace()
                        })
        )
    }

    fun trash()
    {

    }

    fun rename(newName: String, fileId: String)
    {
        var sharedPreferences = SharedPreferencesClient(context)
        var sessionId = sharedPreferences.getString(SharedPreferenceConstants.SessionId)
        if (sessionId != null)
        {
            var fileRenameRequest = FileRenameRequest(sessionId, newName, fileId)
            var repository = OpenDriveRepositoryProvider.provideOpenDriveRepository()
            compositeDisposable.add(
                    repository.fileRename(fileRenameRequest)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                if (newName != result.Name)
                                {
                                    throw Exception("Some Ting Wong")
                                }
                            }, { error ->
                                android.os.Debug.waitForDebugger()
                                error.printStackTrace()
                            })
            )
        }
        throw Exception("Bad Session ID")
    }

    fun restore()
    {

    }
}