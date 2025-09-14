package com.mipa.readerandroid.view.compose

import android.app.Activity
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.view.compose.base.BottomBarSchdule
import com.mipa.readerandroid.view.compose.base.TopBarSchdule



val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("No NavController provided!")
}


@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route

    val window = (LocalContext.current as Activity).window
    SideEffect {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.Transparent.toArgb()
    }

    CompositionLocalProvider(LocalNavController provides navController) {
        Scaffold(
            bottomBar = {
                BottomBarSchdule(currentRoute ?: ConstValue.ROUTER_BOOKMALL)
            },
            topBar = {
                TopBarSchdule(currentRoute ?: ConstValue.ROUTER_BOOKMALL)
            }
            //contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = ConstValue.ROUTER_BOOKMALL,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(ConstValue.ROUTER_BOOKMALL) { BookStoreScreen() }
                composable(ConstValue.ROUTER_MEPAGE) { PersonalProfileScreen() }
                composable(ConstValue.ROUTER_REGISTER) { RegistrationScreen() }
                composable(ConstValue.ROUTER_LOGIN) { LoginScreen() }
                composable(ConstValue.ROUTER_ME_DETAIL) { MeDetailPage() }
                composable(ConstValue.ROUTER_BOOK_DETAIL) { BookDetailScreen() }
                composable(ConstValue.ROUTER_MY_BOOKS){MyBooksScreen()}
            }
        }
    }

}
