package com.alistairfink.betteropendrive;

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.alistairfink.betteropendrive.helpers.SharedPreferencesClient
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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setTextFields(false);
        // checkLogin();
    }

    private fun loginButton(view: View)
    {
        var request = SessionLoginRequest(
            UserName = Login_Email.text.toString(),
            Password = Login_Password.text.toString()
        );
        login(request)
    }

    private fun checkLogin()
    {
        var sharedPreferences = SharedPreferencesClient(this)
        var sessionID = sharedPreferences.getString(SharedPreferenceConstants.SessionId)
        if (sessionID != null)
        {
            checkSessionID(sessionID)
        }
        // TODO : Unlock view at this point for typing in creds
    }

    private fun checkSessionID(sessionID: String)
    {
        var repository = OpenDriveRepositoryProvider.provideOpenDriveRepository()
        var request = SessionExistsRequest(
                SessionId = sessionID
        );
        compositeDisposable.add(
                repository.sessionExists(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            android.os.Debug.waitForDebugger()
                            if (result.Result)
                            {
                                // TODO : Ur good switch views
                            }
                            else
                            {
                                newSessionID()
                            }
                        }, { error ->
                            error.printStackTrace()
                        })
        );
    }

    private fun newSessionID()
    {
        var credentials = getUnencryptedCreds()
        if(credentials ==  null)
        {
            // TODO : Make this not this.
            throw Exception("wtf")
        }
        var request = SessionLoginRequest(
                UserName = credentials!!.UserName,
                Password = credentials!!.Password
        )
        login(request)
    }

    private fun login(request: SessionLoginRequest)
    {
        var repository = OpenDriveRepositoryProvider.provideOpenDriveRepository()
        compositeDisposable.add(
                repository.sessionLogin(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            result ->
                            android.os.Debug.waitForDebugger()
                            var sessionId = result.SessionID
                            var userName = request.UserName
                            var pass = request.Password;
                            var sharedPreferences = SharedPreferencesClient(this)
                            sharedPreferences.writeString(SharedPreferenceConstants.SessionId, sessionId)
                            setEncryptedCreds(Credentials(UserName = userName, Password = pass))
                        },{
                            error ->
                            error.printStackTrace()
                        })
        );
    }

    private fun getUnencryptedCreds() : Credentials?
    {
        var sharedPreferences = SharedPreferencesClient(this)
        var encryptedUser = sharedPreferences.getString(SharedPreferenceConstants.UserName)
        var encryptedPass = sharedPreferences.getString(SharedPreferenceConstants.Password)
        if (encryptedUser == null || encryptedPass == null)
        {
            return null
        }
        // TODO : Unencrypt these
        var unencryptedUser = encryptedUser.toString()
        var unencryptedPass = encryptedPass.toString()
        return Credentials(
                UserName =  unencryptedUser,
                Password =  unencryptedPass
        )
    }

    private fun setEncryptedCreds(creds : Credentials)
    {
        var sharedPreferences = SharedPreferencesClient(this)
        // TODO : Gotta Encrypt these
        var encryptedUser = creds.UserName;
        var encryptedPass = creds.Password;
        sharedPreferences.writeString(SharedPreferenceConstants.UserName, encryptedUser)
        sharedPreferences.writeString(SharedPreferenceConstants.Password, encryptedPass)
    }

    private fun setTextFields(value : Boolean)
    {
        Login_Email.isFocusable = value
        Login_Email.isFocusableInTouchMode = value
        Login_Email.isClickable = value
        Login_Password.isFocusable = value
        Login_Password.isFocusableInTouchMode = value
        Login_Password.isClickable = value
    }
}

data class Credentials
(
        val UserName : String,
        val Password : String
)