package com.alistairfink.betteropendrive;

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.alistairfink.betteropendrive.R.id.Login_Email
import com.alistairfink.betteropendrive.apiService.repositories.OpenDriveRepositoryProvider
import com.alistairfink.betteropendrive.apiService.repositories.TestRepositoryProvider
import com.alistairfink.betteropendrive.requestModels.SessionLoginRequest
import com.alistairfink.betteropendrive.requestModels.TestRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*

class Login : Activity()
{
    private val compositeDisposable: CompositeDisposable = CompositeDisposable();

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    fun Login(view: View)
    {
        var repository = OpenDriveRepositoryProvider.provideOpenDriveRepository();
        var request = SessionLoginRequest(
            UserName = Login_Email.toString(), // THIS DOESN'T WORK
            Password = Login_Password.toString()
        );
        var test = "test";
    }

    fun TestButton(view: View)
    {
        val repository = TestRepositoryProvider.provideTesthRepository();
        var request = TestRequest(Value = "testValue");
        compositeDisposable.add(
                repository.test(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe ({
                            result ->
                            var test = result;
                        }, { error ->
                            error.printStackTrace()
                        })
        )
    }
}