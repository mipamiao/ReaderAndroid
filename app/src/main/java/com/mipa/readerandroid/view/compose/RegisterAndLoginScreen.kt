package com.mipa.readerandroid.view.compose


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AppRegistration
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mipa.readerandroid.view.compose.base.simpleInputCompose
import com.mipa.readerandroid.view.composedata.LoginPageCD
import com.mipa.readerandroid.view.composedata.RegisterPageCD


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    registerPageCD: RegisterPageCD = RegisterPageCD(),
    onRegisterSuccess: (() -> Unit)? = null
) {
    val naviController = LocalNavController.current
    val keyboardController = LocalSoftwareKeyboardController.current
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { registerPageCD.switchToLogin(naviController) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .padding(8.dp)
                        .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(20.dp)
                        )
                ) {
                    Text(
                        text = "用户登录",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
                Text(
                    text = "用户注册",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(8.dp)
                )
            }

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
                    registerPageCD.email = value
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
                    keyboardController?.hide()
                    registerPageCD.register(naviController)
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

@Preview
@Composable
fun LoginScreen(
    loginPageCD: LoginPageCD = LoginPageCD(),
    onLoginClick: (() -> Unit)? = null
) {
    val naviController = LocalNavController.current
    val keyboardController = LocalSoftwareKeyboardController.current
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "用户登录",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(8.dp)
                )
                Button(
                    onClick = { loginPageCD.switchToRegister(naviController) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .padding(8.dp)
                        .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(20.dp)
                        )
                ) {
                    Text(
                        text = "用户注册",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
            }


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
                    keyboardController?.hide()
                    loginPageCD.login(naviController)
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

//@Composable
//fun AuthNavigation() {
//    var currentScreen  by remember { mutableStateOf(AuthScreen.LOGIN) }
//    // 定义页面顺序用于判断动画方向
//    val screenOrder = listOf(AuthScreen.LOGIN, AuthScreen.REGISTRATION)
//
//    val naviController = LocalNavController.current
//
//    val loginPageCD = LoginPageCD()
//    val registerPageCD = RegisterPageCD()
//
//    Scaffold(
//        bottomBar = {
//            BottomAppBar {
//                NavigationBarItem(
//                    selected = currentScreen == AuthScreen.LOGIN,
//                    onClick = { currentScreen = AuthScreen.LOGIN },
//                    icon = { Icon(Icons.Default.AccountCircle, "登录") },
//                    label = { Text("登录") }
//                )
//                NavigationBarItem(
//                    selected = currentScreen == AuthScreen.REGISTRATION,
//                    onClick = { currentScreen = AuthScreen.REGISTRATION },
//                    icon = { Icon(Icons.Default.AppRegistration, "注册") },
//                    label = { Text("注册") }
//                )
//            }
//        }
//    ) {
//            paddingValues ->
//        AnimatedContent(
//            targetState = currentScreen,
//            transitionSpec = {
//                // 根据页面顺序判断动画方向
//                val currentIndex = screenOrder.indexOf(initialState)
//                val targetIndex = screenOrder.indexOf(targetState)
//
//                if (targetIndex > currentIndex) {
//                    // 目标页面在右侧 - 从右侧滑入
//                    slideInHorizontally(animationSpec = tween(300)) { it }
//                        .togetherWith(
//                            slideOutHorizontally(animationSpec = tween(300)) { -it }
//                        )
//                } else {
//                    // 目标页面在左侧 - 从左侧滑入
//                    slideInHorizontally(animationSpec = tween(300)) { -it }
//                        .togetherWith(
//                            slideOutHorizontally(animationSpec = tween(300)) { it }
//                        )
//                }
//            },
//            modifier = Modifier.padding(paddingValues)
//        ) {
//                screen ->
//            when (screen) {
//                AuthScreen.LOGIN -> loginScreen(
//                    loginPageCD = loginPageCD,
//                    onLoginClick = { Thread {loginPageCD.login(naviController)}.start() }
//                )
//                AuthScreen.REGISTRATION -> registrationScreen(
//                    registerPageCD = registerPageCD,
//                    onRegisterSuccess = { Thread {registerPageCD.register(currentScreen)}.start() }
//                )
//            }
//        }
//    }
//}