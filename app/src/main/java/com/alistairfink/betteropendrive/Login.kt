package com.alistairfink.betteropendrive;
import kotlinx.android.synthetic.main.activity_login.*;

import android.app.Activity
import android.os.Bundle
import android.view.View

class Login : Activity()
{
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
        var test = Login_Email.text.toString();
    }
}