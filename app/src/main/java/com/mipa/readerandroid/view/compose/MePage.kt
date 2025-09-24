package com.mipa.readerandroid.view.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.mipa.readerandroid.R
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.model.feature.UserProfile
import com.mipa.readerandroid.view.compose.base.IconAndTextItem
import com.mipa.readerandroid.view.composedata.MePageCD

@Composable
fun PersonalProfileScreen() {

    val viewModel = CDMap.get<MePageCD>()
    val userProfile  = viewModel.userProfile.value
    val avatarUrl = viewModel.avatarUrl

    val naviConttoller = LocalNavController.current
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth()
                .clickable { viewModel.onClickUserProfile(naviConttoller) },
            verticalAlignment = Alignment.Top
        ) {
            // 左侧圆形头像
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            ) {
                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(avatarUrl.value)
                        .placeholder(R.drawable.offline_avatar)
                        .error(R.drawable.offline_avatar)
                        .crossfade(true)
                        .build(),
                )
                Image(
                    painter = painter,
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
                    text = userProfile.userName?:"点击登录",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = userProfile.role?:"",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = userProfile.email?:"",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = userProfile.userId?:"",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
        }
        Column {
            if(viewModel.isLogin())
                IconAndTextItem(Icons.AutoMirrored.Filled.MenuBook, "我的书籍", onClick = {
                    viewModel.onClickMyBooksItem(naviConttoller)
                })
        }
    }

}