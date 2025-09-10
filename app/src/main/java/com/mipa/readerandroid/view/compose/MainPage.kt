package com.mipa.readerandroid.view.compose

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.with
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mipa.readerandroid.base.ConstValue


// 添加新的导航枚举
enum class MainScreen {
    BOOK_STORE,
    PERSONAL_PROFILE
}

val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("No NavController provided!")
}

// 实现底部导航组件
@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun MainNavigation() {
    var currentScreen by remember { mutableStateOf(MainScreen.BOOK_STORE) }
    val decayAnimationSpec = rememberSplineBasedDecay<Float>();
    val navController = rememberNavController()


    CompositionLocalProvider(LocalNavController provides navController){
        Scaffold(
            bottomBar = {
                NavigationBar  {
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Book, contentDescription = "书城") },
                        label = { Text("书城") },
                        selected = currentScreen == MainScreen.BOOK_STORE,
                        onClick = {
                            currentScreen = MainScreen.BOOK_STORE
                            navController.navigate(ConstValue.ROUTER_BOOKMALL){
                                launchSingleTop = true
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Person, contentDescription = "个人中心") },
                        label = { Text("个人中心") },
                        selected = currentScreen == MainScreen.PERSONAL_PROFILE,
                        onClick = {
                            currentScreen = MainScreen.PERSONAL_PROFILE
                            navController.navigate(ConstValue.ROUTER_MEPAGE){
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        ) {paddingValues ->
            NavHost(
                navController = navController,
                startDestination = ConstValue.ROUTER_BOOKMALL, // 初始页面
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(ConstValue.ROUTER_BOOKMALL) { BookStoreScreen() }
                composable(ConstValue.ROUTER_MEPAGE) { PersonalProfileScreen() }
                composable(ConstValue.ROUTER_REGISTER) { RegistrationScreen() }
                composable(ConstValue.ROUTER_LOGIN){ LoginScreen() }
                composable(ConstValue.ROUTER_ME_DETAIL){ MeDetailPage() }
            }
        }
    }

}
