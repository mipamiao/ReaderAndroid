package com.mipa.readerandroid.view.compose

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AppRegistration
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mipa.readerandroid.view.compose.base.simpleInputCompose
import com.mipa.readerandroid.view.composedata.LoginPageCD
import com.mipa.readerandroid.view.composedata.RegisterPageCD


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun registrationScreen(
    registerPageCD: RegisterPageCD,
    onRegisterSuccess: () -> Unit
) {

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
            Text(
                text = "用户注册",
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            simpleInputCompose(
                label = "用户名",
                text = registerPageCD.name,
                onTextChange = { value ->
                    registerPageCD.name = value
                })

            Spacer(modifier = Modifier.height(12.dp))

            simpleInputCompose(
                label = "邮箱",
                text = registerPageCD.email,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                onTextChange = { value ->
                    registerPageCD.name = value
                })

            Spacer(modifier = Modifier.height(12.dp))

            simpleInputCompose(
                label = "密码",
                text = registerPageCD.password,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                onTextChange = { value ->
                    registerPageCD.password = value
                })

            Spacer(modifier = Modifier.height(12.dp))

            // 注册按钮
            Button(
                onClick = {
                    onRegisterSuccess()
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
fun loginScreen(
    loginPageCD: LoginPageCD,
    onLoginClick: () -> Unit
) {
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
            Text(
                text = "用户登录",
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            simpleInputCompose(label = "用户名", text = loginPageCD.name, onTextChange = { value ->
                loginPageCD.name = value
            })


            Spacer(modifier = Modifier.height(12.dp))

            simpleInputCompose(
                label = "密码",
                text = loginPageCD.password,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                onTextChange = { value ->
                    loginPageCD.password = value
                })

            Spacer(modifier = Modifier.height(12.dp))

            // 注册按钮
            Button(
                onClick = {
                    onLoginClick()
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

// 添加导航状态枚举
enum class AuthScreen { LOGIN, REGISTRATION }

// 添加导航主界面
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AuthNavigation(
    loginPageCD: LoginPageCD,
    registerPageCD: RegisterPageCD
) {
    var currentScreen by remember { mutableStateOf(AuthScreen.LOGIN) }
    // 定义页面顺序用于判断动画方向
    val screenOrder = listOf(AuthScreen.LOGIN, AuthScreen.REGISTRATION)

    Scaffold(
        bottomBar = {
            BottomAppBar {
                NavigationBarItem(
                    selected = currentScreen == AuthScreen.LOGIN,
                    onClick = { currentScreen = AuthScreen.LOGIN },
                    icon = { Icon(Icons.Default.AccountCircle, "登录") },
                    label = { Text("登录") }
                )
                NavigationBarItem(
                    selected = currentScreen == AuthScreen.REGISTRATION,
                    onClick = { currentScreen = AuthScreen.REGISTRATION },
                    icon = { Icon(Icons.Default.AppRegistration, "注册") },
                    label = { Text("注册") }
                )
            }
        }
    ) {
            paddingValues ->
        AnimatedContent(
            targetState = currentScreen,
            transitionSpec = {
                // 根据页面顺序判断动画方向
                val currentIndex = screenOrder.indexOf(initialState)
                val targetIndex = screenOrder.indexOf(targetState)

                if (targetIndex > currentIndex) {
                    // 目标页面在右侧 - 从右侧滑入
                    slideInHorizontally(animationSpec = tween(300)) { it }
                        .togetherWith(
                            slideOutHorizontally(animationSpec = tween(300)) { -it }
                        )
                } else {
                    // 目标页面在左侧 - 从左侧滑入
                    slideInHorizontally(animationSpec = tween(300)) { -it }
                        .togetherWith(
                            slideOutHorizontally(animationSpec = tween(300)) { it }
                        )
                }
            },
            modifier = Modifier.padding(paddingValues)
        ) {
                screen ->
            when (screen) {
                AuthScreen.LOGIN -> loginScreen(
                    loginPageCD = loginPageCD,
                    onLoginClick = { Thread {loginPageCD.login()}.start() }
                )
                AuthScreen.REGISTRATION -> registrationScreen(
                    registerPageCD = registerPageCD,
                    onRegisterSuccess = { Thread {registerPageCD.register()}.start() }
                )
            }
        }
    }
}