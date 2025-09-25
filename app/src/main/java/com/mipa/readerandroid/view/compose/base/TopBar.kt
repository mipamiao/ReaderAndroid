package com.mipa.readerandroid.view.compose.base

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.view.compose.LocalNavController
import com.mipa.readerandroid.view.composedata.ChapterListPageCD
import com.mipa.readerandroid.view.composedata.MyChaptersPageCD

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailPageTopBar() {

    val naviController = LocalNavController.current

    TopAppBar(
        title = {
            Text(
                "书籍详情",
                modifier = Modifier
                    .padding(0.dp)
                    .wrapContentHeight(Alignment.CenterVertically) // 标题垂直居中
            )
        },
        modifier = Modifier
            .padding(0.dp)
            .height(64.dp),
        navigationIcon = {
            IconButton(
                onClick = { naviController.navigate(ConstValue.ROUTER_BOOKMALL){
                    popUpTo(0)
                } },
                modifier = Modifier
                    .padding(0.dp)
                    .fillMaxHeight() // 填满高度
                    .wrapContentHeight(Alignment.CenterVertically) // 图标按钮垂直居中
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "返回",
                    modifier = Modifier.padding(0.dp)
                )
            }
        },
        actions = {
            // 分享按钮
            IconButton(
                onClick = { /* 分享功能 */ },
                modifier = Modifier
                    .padding(0.dp)
                    .fillMaxHeight()
                    .wrapContentHeight(Alignment.CenterVertically)
            ) {
                Icon(
                    Icons.Default.Share,
                    contentDescription = "分享",
                    modifier = Modifier.padding(0.dp)
                )
            }
            // 收藏按钮
            IconButton(
                onClick = { /* 收藏功能 */ },
                modifier = Modifier
                    .padding(0.dp)
                    .fillMaxHeight()
                    .wrapContentHeight(Alignment.CenterVertically)
            ) {
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = "收藏",
                    modifier = Modifier.padding(0.dp)
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterListPageTopBar() {
    val naviController = LocalNavController.current
    var viewModel = CDMap.get<ChapterListPageCD>()
    TopAppBar(
        title = { Text(viewModel.book.value.title ?: "default") },
        navigationIcon = {
            IconButton(onClick = { viewModel.onBackClick(naviController) }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "返回")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyChapterListPageTopBar(){
    val viewModel = CDMap.get<MyChaptersPageCD>()
    val chapters = viewModel.datas
    val currentBook by viewModel.book.collectAsState()

    val naviController = LocalNavController.current
    TopAppBar(
        title = {
            Column {
                Text(currentBook.title ?: "default", maxLines = 1, fontSize = 16.sp)
                Text("章节管理 (${viewModel.chapterNum}章)", maxLines = 1, fontSize = 12.sp)
            }
        },
        navigationIcon = {
            IconButton(onClick = { naviController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "返回")
            }
        },
        actions = {
            // 添加章节按钮
            IconButton(onClick = { viewModel.onAddClick(naviController) }) {
                Icon(Icons.Default.Add, contentDescription = "添加章节")
            }
        }
    )
}

@Composable
fun TopBarSchdule(Router: String){
    when (Router) {
        ConstValue.ROUTER_BOOK_DETAIL -> {
            BookDetailPageTopBar()
        }

        ConstValue.ROUTER_CHAPTER_LIST -> {
            ChapterListPageTopBar()
        }

        ConstValue.ROUTER_MY_CHAPTERS_LIST -> {
            MyChapterListPageTopBar()
        }

        else -> {}
    }
}