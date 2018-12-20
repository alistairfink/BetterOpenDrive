package com.alistairfink.betteropendrive;

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.view.View
import com.alistairfink.betteropendrive.apiService.repositories.MiscellaneousRepositoryProvider
import com.alistairfink.betteropendrive.helpers.SharedPreferencesClient
import com.alistairfink.betteropendrive.requestModels.SessionExistsRequest
import com.alistairfink.betteropendrive.apiService.repositories.OpenDriveRepositoryProvider
import com.alistairfink.betteropendrive.helpers.EncryptedCredentials
import com.alistairfink.betteropendrive.helpers.EncryptionData
import com.alistairfink.betteropendrive.helpers.EncryptionHelper
import com.alistairfink.betteropendrive.requestModels.SessionLoginRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*

class Login : Activity()
{
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        EncryptionHelper.generateSecretKey()
        setTextFields(false)
        checkLogin()
    }

    @Suppress("UNUSED_PARAMETER")
    fun loginButton(view: View)
    {
        var request = SessionLoginRequest(
            UserName = Login_Email.text.toString(),
            Password = Login_Password.text.toString()
        )
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
        setTextFields(true)
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
                            if (result.Result)
                            {
                                loginSuccess()
                            }
                            else
                            {
                                newSessionID()
                            }
                        }, { error ->
                            error.printStackTrace()
                        })
        )
    }

    private fun newSessionID()
    {
        var credentials = getUnencryptedCreds()
        if(credentials ==  null)
        {
            var sharedPrefs = SharedPreferencesClient(this)
            sharedPrefs.removeKey(SharedPreferenceConstants.UserName)
            sharedPrefs.removeKey(SharedPreferenceConstants.UserNameIV)
            sharedPrefs.removeKey(SharedPreferenceConstants.Password)
            sharedPrefs.removeKey(SharedPreferenceConstants.PasswordIV)
            setTextFields(true)
            return
        }
        var request = SessionLoginRequest(
                UserName = credentials.UserName,
                Password = credentials.Password
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
                            setTextFields(false)
                            var sessionId = result.SessionID
                            var userName = request.UserName
                            var pass = request.Password;
                            var name = result.UserFirstName + " " + result.userLastName
                            var sharedPreferences = SharedPreferencesClient(this)
                            sharedPreferences.writeString(SharedPreferenceConstants.SessionId, sessionId)
                            setEncryptedCreds(Credentials(UserName = userName, Password = pass))
                            sharedPreferences.writeString(SharedPreferenceConstants.Name, name)
                            getProfileInfo(sessionId)
                        },{
                            error ->
                            android.os.Debug.waitForDebugger()
                            error.printStackTrace()
                        })
        )
    }

    private fun getProfileInfo(sessionId: String)
    {
        var repository = OpenDriveRepositoryProvider.provideOpenDriveRepository()
        compositeDisposable.add(
                repository.usersInfo(sessionId)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            android.os.Debug.waitForDebugger()
                            var repo = MiscellaneousRepositoryProvider.provideMiscellaneousApiServiceRepository()
                            compositeDisposable.add(
                                    repo.getImage(result.Avatar)
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribeOn(Schedulers.io())
                                            .subscribe({ response ->
                                                android.os.Debug.waitForDebugger()
                                                var imageAsString = Base64.encodeToString(response.bytes(), Base64.DEFAULT)
                                                var sharedPreferences = SharedPreferencesClient(this)
                                                sharedPreferences.writeString(SharedPreferenceConstants.Avatar, imageAsString)
                                                loginSuccess()
                                            }, { error ->
                                                error.printStackTrace()
                                            })
                            )
                        }, { error ->
                            error.printStackTrace()
                        })
        )
    }

    private fun getUnencryptedCreds() : Credentials?
    {
        var sharedPreferences = SharedPreferencesClient(this)
        var encryptedCredentials = sharedPreferences.getEncryptedCredentials()
        if (encryptedCredentials.UserName == null ||
                encryptedCredentials.UserNameIV == null ||
                encryptedCredentials.Password == null ||
                encryptedCredentials.PasswordIV == null)
        {
            return null
        }
        var unencryptedUser = EncryptionHelper.decrypt(EncryptionData(encryptedCredentials.UserName.toString(), encryptedCredentials.UserNameIV.toString()))
        var unencryptedPass = EncryptionHelper.decrypt(EncryptionData(encryptedCredentials.Password.toString(), encryptedCredentials.PasswordIV.toString()))
        return Credentials(
                UserName =  unencryptedUser,
                Password =  unencryptedPass
        )
    }

    private fun setEncryptedCreds(creds : Credentials)
    {
        var sharedPreferences = SharedPreferencesClient(this)
        var encryptedUser = EncryptionHelper.encrypt(creds.UserName)
        var encryptedPass = EncryptionHelper.encrypt(creds.Password)
        sharedPreferences.writeEncryptedCredentials(EncryptedCredentials(
                UserName = encryptedUser.Value,
                UserNameIV = encryptedUser.IV,
                Password = encryptedPass.Value,
                PasswordIV = encryptedPass.IV
        ))
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

    private fun loginSuccess()
    {
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

// TODO : Move this in class
data class Credentials
(
        val UserName : String,
        val Password : String
)