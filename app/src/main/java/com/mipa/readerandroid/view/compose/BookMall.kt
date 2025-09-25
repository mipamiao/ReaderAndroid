package com.mipa.readerandroid.view.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.view.compose.base.BookItem
import com.mipa.readerandroid.view.compose.base.LoadingCompose
import com.mipa.readerandroid.view.composedata.BookMallCD


@Preview(showBackground = true)
@Composable
fun BookMallScreen() {
    val viewModel =  CDMap.get<BookMallCD>()
    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    val books = viewModel.datas
    val isLoading by viewModel.isLoading.collectAsState()
    val hasMoreData by viewModel.hasMoreData.collectAsState()

    val gridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()


    val naviController = LocalNavController.current

    // 检测是否滑动到底部
    val isAtBottom = remember {
        derivedStateOf {
            val lastVisibleItemIndex = gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
            val totalItemsCount = gridState.layoutInfo.totalItemsCount
            lastVisibleItemIndex != null && lastVisibleItemIndex == totalItemsCount - 1
        }
    }


    if (isAtBottom.value && !isLoading && hasMoreData) {
        LaunchedEffect(true) {
            viewModel.loadMoreDatas()
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "书城",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }


            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                state = gridState,
                modifier = Modifier.fillMaxSize()
            ) {
                items(books.size) { index ->
                    BookItem(
                        book = books[index],
                        onBookClick = {
                            viewModel.onItemClick(it, naviController)
                        }
                    )
                }

                // 加载指示器
                if (isLoading) {
                    item {
                        LoadingCompose()
                    }
                }

                // 没有更多数据时显示
                if (!hasMoreData && books.isNotEmpty()) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(text = "已经到底啦~")
                        }
                    }
                }
            }
        }
    }
}