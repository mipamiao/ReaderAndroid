package com.mipa.readerandroid.view.activity

import android.os.Bundle
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
import com.mipa.readerandroid.view.compose.RegistrationScreen
import com.mipa.readerandroid.ui.theme.ReaderAndroidTheme

class MainActivity :AbsActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReaderAndroidTheme {
                TestViewModel(UserProfileViewModel())
            }
        }
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

@Composable
fun PersonalProfileScreen() {
    Row(
        modifier = Modifier
            .height(120.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        // 左侧圆形头像
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile_avatar), // 确保在res/drawable目录下有此图片
                contentDescription = "个人头像",
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.width(24.dp))

        // 右侧个人信息文本
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "张三",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Android 开发工程师",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "zhang.san@example.com",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "+86 138 **** 5678",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
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

@Preview(showBackground = true)
@Composable
fun showRegister(){
    ReaderAndroidTheme {
        RegistrationScreen {  }
    }
}


