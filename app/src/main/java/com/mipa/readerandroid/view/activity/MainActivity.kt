package com.mipa.readerandroid.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mipa.model.UserProfileViewModel
import com.mipa.readerandroid.R
import com.mipa.readerandroid.base.AbsActivity
import com.mipa.readerandroid.model.dto.UserLoginRequest
import com.mipa.readerandroid.model.dto.UserRegisterRequest
import com.mipa.readerandroid.service.UserService
import com.mipa.readerandroid.view.compose.registrationScreen
import com.mipa.readerandroid.ui.theme.ReaderAndroidTheme
import com.mipa.readerandroid.view.compose.AuthNavigation
import com.mipa.readerandroid.view.compose.PersonalProfileScreen
import com.mipa.readerandroid.view.compose.loginScreen
import com.mipa.readerandroid.view.composedata.LoginPageCD
import com.mipa.readerandroid.view.composedata.RegisterPageCD

class MainActivity :AbsActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReaderAndroidTheme {
                PersonalProfilePreview()
            }
        }
        startActivity(Intent(this, RegisterAndLoginActivity::class.java))
    }
}

@Composable
fun TestViewModel(viewModel: UserProfileViewModel){
    ReaderAndroidTheme {
        Button(onClick =  {viewModel.login()}) {
            if(viewModel.isLogin.value)
                Text("登录")
            else Text("退出登录")
        }
    }

}



@Preview(showBackground = true)
@Composable
fun PersonalProfilePreview() {
    ReaderAndroidTheme {
        PersonalProfileScreen()
    }
}



