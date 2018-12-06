package com.alistairfink.betteropendrive;

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.alistairfink.betteropendrive.helpers.SharedPreferencesHelper
import com.alistairfink.betteropendrive.requestModels.SessionExistsRequest
import com.alistairfink.betteropendrive.apiService.repositories.OpenDriveRepositoryProvider
import com.alistairfink.betteropendrive.requestModels.SessionLoginRequest
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
        // checkSessionId();
    }

    fun login(view: View)
    {
        var repository = OpenDriveRepositoryProvider.provideOpenDriveRepository();
        var request = SessionLoginRequest(
            UserName = Login_Email.text.toString(),
            Password = Login_Password.text.toString()
        );
        compositeDisposable.add(
                repository.sessionLogin(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            result ->
                            android.os.Debug.waitForDebugger();
                            var sessionId = result.SessionID;
                            var userName = request.UserName;
                            var pass = request.Password;
                            // TODO : Finish this
                            // Make a helper function for shared preferences
                            var sharedPreferencesHelper = SharedPreferencesHelper(this);
                            var test1 = sharedPreferencesHelper.getString("test");
                            sharedPreferencesHelper.writeString("test", userName);
                            var test2 = sharedPreferencesHelper.getString("test");
                            var test3 = "";
                        },{
                            error ->
                            android.os.Debug.waitForDebugger();
                            error.printStackTrace();
                        })
        );
    }

    fun checkSessionId()
    {
        // TODO : Get session id from storage/check if there is one



        var repository = OpenDriveRepositoryProvider.provideOpenDriveRepository();
        var request = SessionExistsRequest(
            SessionId =  ""
        );


        compositeDisposable.add(
                repository.sessionExists(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            result ->
                            android.os.Debug.waitForDebugger();
                            // TODO : Figure out how to work this since i can't return parent function here

                        },{
                            error ->
                            android.os.Debug.waitForDebugger();
                            error.printStackTrace();
                        })
        );
    }
}