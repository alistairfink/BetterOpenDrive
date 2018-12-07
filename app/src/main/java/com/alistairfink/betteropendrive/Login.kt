package com.alistairfink.betteropendrive;

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.alistairfink.betteropendrive.SharedPreferenceConstants.Companion.SessionId
import com.alistairfink.betteropendrive.apiService.repositories.OpenDriveRepository
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

    fun loginButton(view: View)
    {
        var request = SessionLoginRequest(
            UserName = Login_Email.text.toString(),
            Password = Login_Password.text.toString()
        );
        login(request);
    }

    fun checkLogin()
    {
        var sharedPreferencesHelper = SharedPreferencesHelper(this);
        var sessionID = sharedPreferencesHelper.getString(SharedPreferenceConstants.SessionId);
        if (sessionID != null)
        {
            checkSessionID(sessionID);
        }
    }

    fun checkSessionID(sessionID: String)
    {
        var repository = OpenDriveRepositoryProvider.provideOpenDriveRepository();
        var request = SessionExistsRequest(
                SessionId = sessionID
        );
        compositeDisposable.add(
                repository.sessionExists(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            android.os.Debug.waitForDebugger();
                            if (result.Result)
                            {
                                // TODO : Login here
                            }
                            else
                            {
                                // TODO : Get new SessionID here
                            }
                        }, { error ->
                            error.printStackTrace();
                        })
        );
    }

    fun newSessionID()
    {
        var sharedPreferences = SharedPreferencesHelper(this);
        var encryptedUser = sharedPreferences.getString(SharedPreferenceConstants.UserName);
        var encryptedPass = sharedPreferences.getString(SharedPreferenceConstants.Password);
        // TODO : Unencrypt these
        var user = encryptedUser.toString();
        var pass = encryptedPass.toString();
        var request = SessionLoginRequest(
                UserName = user,
                Password = pass
        );
        login(request);
    }

    fun login(request: SessionLoginRequest)
    {
        var repository = OpenDriveRepositoryProvider.provideOpenDriveRepository();
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
                            var sharedPreferencesHelper = SharedPreferencesHelper(this);
                            sharedPreferencesHelper.writeString(SharedPreferenceConstants.SessionId, sessionId);
                            // TODO : Gotta Encrypt these
                            var encryptedUsername = "";
                            var encryptedPass = "";
                            sharedPreferencesHelper.writeString(SharedPreferenceConstants.UserName, encryptedUsername);
                            sharedPreferencesHelper.writeString(SharedPreferenceConstants.Password, encryptedPass);
                        },{
                            error ->
                            error.printStackTrace();
                        })
        );
    }
}