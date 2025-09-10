package com.mipa.readerandroid.view.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mipa.readerandroid.base.AbsActivity
import com.mipa.readerandroid.ui.theme.ReaderAndroidTheme
import com.mipa.readerandroid.view.compose.RegistrationScreen
import com.mipa.readerandroid.view.composedata.LoginPageCD
import com.mipa.readerandroid.view.composedata.MePageCD
import com.mipa.readerandroid.view.composedata.RegisterPageCD

class RegisterAndLoginActivity:AbsActivity() {
    private val registerPageCD  = RegisterPageCD()
    private val loginPageCD = LoginPageCD()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReaderAndroidTheme {
                RegistrationScreen()
            }
        }
        MePageCD.registerAndLoginActivity = this

    }
}