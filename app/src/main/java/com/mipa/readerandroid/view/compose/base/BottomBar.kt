package com.mipa.readerandroid.view.compose.base

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.view.compose.LocalNavController
import com.mipa.readerandroid.view.composedata.BookShelfPageCD

@Composable
fun MainPageBottomBar(){
    val naviController = LocalNavController.current
    val currentRoute = naviController.currentDestination?.route

    NavigationBar  {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Book, contentDescription = "书城") },
            label = { Text("书城") },
            selected = currentRoute.equals(ConstValue.ROUTER_BOOKMALL),
            onClick = {
                naviController.navigate(ConstValue.ROUTER_BOOKMALL){
                    launchSingleTop = true
                    popUpTo(0)
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Bed, contentDescription = "书架") },
            label = { Text("书架") },
            selected = currentRoute.equals(ConstValue.ROUTER_BOOK_SHELF),
            onClick = {
                CDMap.get<BookShelfPageCD>().needFlush()
                naviController.navigate(ConstValue.ROUTER_BOOK_SHELF){
                    launchSingleTop = true
                    popUpTo(0)
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Search, contentDescription = "搜索") },
            label = { Text("搜索") },
            selected = currentRoute.equals(ConstValue.ROUTER_SEARCH_PAGE),
            onClick = {
                naviController.navigate(ConstValue.ROUTER_SEARCH_PAGE){
                    launchSingleTop = true
                    popUpTo(0)
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "个人中心") },
            label = { Text("个人中心") },
            selected = currentRoute.equals(ConstValue.ROUTER_MEPAGE),
            onClick = {
                naviController.navigate(ConstValue.ROUTER_MEPAGE){
                    launchSingleTop = true
                    popUpTo(0)
                }
            }
        )
    }
}

@Composable
fun BottomBarSchdule(Router: String) {
    when (Router) {
        ConstValue.ROUTER_BOOKMALL,
        ConstValue.ROUTER_MEPAGE,
        ConstValue.ROUTER_BOOK_SHELF,
        ConstValue.ROUTER_SEARCH_PAGE-> {
            MainPageBottomBar()
        }

        else -> {}
    }
}