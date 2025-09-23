package com.mipa.readerandroid.view.compose

import android.app.Activity
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
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
import com.mipa.readerandroid.view.compose.writer.WriterView
import com.mipa.readerandroid.view.reader.ReaderScreen


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
                modifier = Modifier.padding(paddingValues),
                enterTransition = { fadeIn() },
                exitTransition = { fadeOut() },
            ) {
                composable(ConstValue.ROUTER_BOOKMALL) { BookMallScreen() }
                composable(ConstValue.ROUTER_MEPAGE) { PersonalProfileScreen() }
                composable(ConstValue.ROUTER_AUTH_PAGE){ AuthScreen() }
                composable(ConstValue.ROUTER_ME_DETAIL) { MeDetailPage() }
                composable(ConstValue.ROUTER_BOOK_DETAIL) { BookDetailScreen() }
                composable(ConstValue.ROUTER_MY_BOOKS){MyBooksScreen()}
                composable(ConstValue.ROUTER_CHAPTER_LIST){ ChapterListScreen() }
                composable(ConstValue.ROUTER_MY_CHAPTERS_LIST){ MyChaptersPageScreen() }
                composable(ConstValue.ROUTER_READER_PAGE){ ReaderScreen() }
                composable(ConstValue.ROUTER_WRITER_PAGE){ WriterView() }
                composable(ConstValue.ROUTER_BOOK_SHELF){ BookshelfScreen() }
                composable(ConstValue.ROUTER_SEARCH_PAGE){ SearchScreen()}
            }
        }
    }

}
