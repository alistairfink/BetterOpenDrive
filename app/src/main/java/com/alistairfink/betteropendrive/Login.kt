package com.alistairfink.betteropendrive;
import kotlinx.android.synthetic.main.activity_login.*;

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.alistairfink.betteropendrive.ApiService.repositories.TestRepositoryProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class Login : Activity()
{
    private val compositeDisposable: CompositeDisposable = CompositeDisposable();

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    fun CheckLogin(userName:String, pass:String)
    {
        Constants.OpenDriveBaseURL;
    }

    fun TestButton(view: View)
    {
        val repository = TestRepositoryProvider.provideTesthRepository()
        compositeDisposable.add(
                repository.test("testValue")
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe ({
                            result ->
                            Log.d("Result", "There are ${result.testResponse.message} Java developers in Lagos")
                        }, { error ->
                            error.printStackTrace()
                        })
        )
    }
}