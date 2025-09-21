package com.mipa.readerandroid.view.compose


import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mipa.readerandroid.view.compose.base.simpleInputCompose



import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.graphics.Color
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.view.composedata.AuthPageCD


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(viewModel: AuthPageCD) {
    var name = viewModel.register_name
    var email = viewModel.register_email
    var password = viewModel.register_password

    val keyboardController = LocalSoftwareKeyboardController.current
    val navController = LocalNavController.current

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            simpleInputCompose(
                label = "用户名",
                text = name.value,
                onTextChange = { value ->
                    name.value = value
                })

            Spacer(modifier = Modifier.height(12.dp))

            simpleInputCompose(
                label = "邮箱",
                text = email.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                onTextChange = { value ->
                    email.value = value
                })

            Spacer(modifier = Modifier.height(12.dp))

            simpleInputCompose(
                label = "密码",
                text = password.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                onTextChange = { value ->
                    password.value = value
                })

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    keyboardController?.hide()
                    viewModel.register(navController)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = "注册")
            }
        }
    }
}

@Composable
fun LoginScreen(viewModel: AuthPageCD) {
    var name = viewModel.login_name
    var password = viewModel.login_password

    val keyboardController = LocalSoftwareKeyboardController.current
    val navController = LocalNavController.current
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            simpleInputCompose(label = "用户名", text = name.value, onTextChange = { value ->
                name.value = value
            })


            Spacer(modifier = Modifier.height(12.dp))

            simpleInputCompose(
                label = "密码",
                text = password.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                onTextChange = { value ->
                    password.value = value
                })

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    keyboardController?.hide()
                    viewModel.login(navController)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = "登录")
            }
        }
    }
}

@Composable
fun AuthTopBar(
    viewModel: AuthPageCD,
    onBackPress: () -> Unit
) {
    val isLoginScreen = viewModel.isLoginScreen
    // 动画颜色状态
    val loginColor by animateColorAsState(
        targetValue = if (isLoginScreen.value) MaterialTheme.colorScheme.primary else Color.Gray,
        animationSpec = tween(durationMillis = 300)
    )

    val registerColor by animateColorAsState(
        targetValue = if (isLoginScreen.value) Color.Gray else MaterialTheme.colorScheme.primary,
        animationSpec = tween(durationMillis = 300)
    )

    // 下划线动画宽度
    val indicatorWidth by animateDpAsState(
        targetValue = if (isLoginScreen.value) 60.dp else 80.dp,
        animationSpec = tween(durationMillis = 300)
    )

    val indicatorOffset by animateDpAsState(
        targetValue = if (isLoginScreen.value) 20.dp else 100.dp,
        animationSpec = tween(durationMillis = 300)
    )

    // 主容器
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // 返回按钮和标题区域
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackPress,
                modifier = Modifier
                    .size(40.dp)
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "返回",
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "欢迎使用",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(40.dp)) // 为了标题居中
        }

        // 切换标签区域
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            // 标签选项
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "登录",
                    color = loginColor,
                    fontSize = 18.sp,
                    fontWeight = if (isLoginScreen.value) MaterialTheme.typography.titleMedium.fontWeight else MaterialTheme.typography.bodyMedium.fontWeight,
                    modifier = Modifier
                        .padding(20.dp)
                        .clickable {
                            viewModel.switchToLogin()
                        }
                )
                Text(
                    text = "注册",
                    color = registerColor,
                    fontSize = 18.sp,
                    fontWeight = if (isLoginScreen.value) MaterialTheme.typography.bodyMedium.fontWeight else MaterialTheme.typography.titleMedium.fontWeight,
                    modifier = Modifier
                        .padding(20.dp)
                        .clickable {
                            viewModel.switchToRegister()
                        }
                )
            }

//            // 指示器下划线
//            Spacer(
//                modifier = Modifier
//                    .offset(x = indicatorOffset)
//                    .width(indicatorWidth)
//                    .height(3.dp)
//                    .background(MaterialTheme.colorScheme.primary)
//                    .align(Alignment.BottomCenter)
//            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthScreen() {

    val naviController = LocalNavController.current
    val viewModel = CDMap.get<AuthPageCD>()
    // 状态管理
    var isLoginScreen  = viewModel.isLoginScreen

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // 使用我们的顶部栏组件
            AuthTopBar(
                viewModel,
                onBackPress = {naviController.popBackStack()}
            )
            // 根据当前模式显示不同的内容
            if (isLoginScreen.value) {
                LoginScreen(viewModel)
            } else {
                RegisterScreen(viewModel)
            }
        }
    }
}