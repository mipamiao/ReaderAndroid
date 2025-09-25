package com.mipa.readerandroid.view.compose


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.view.compose.base.ChapterItem
import com.mipa.readerandroid.view.compose.base.LoadingCompose
import com.mipa.readerandroid.view.composedata.ChapterListPageCD
import com.mipa.readerandroid.view.reader.ReaderViewCD


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterListScreen() {
    val viewModel = CDMap.get<ChapterListPageCD>()
    val chapters = viewModel.datas
    val isLoading by viewModel.isLoading.collectAsState()
    val hasMoreData by viewModel.hasMoreData.collectAsState()
    val currentBook by viewModel.book.collectAsState()

    var naviController = LocalNavController.current

    val listState = rememberLazyListState()

    LaunchedEffect(viewModel.flushFlag) { viewModel.refresh() }

    val isAtBottom = remember {
        derivedStateOf {
            val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
            val totalItemsCount = listState.layoutInfo.totalItemsCount
            lastVisibleItemIndex != null && lastVisibleItemIndex == totalItemsCount - 1
        }
    }

    if (isAtBottom.value && !isLoading && hasMoreData) {
        LaunchedEffect(true) {
            viewModel.loadMoreDatas()
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column {
            ChapterListHeader(chapters.size)
            // 章节列表
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                state = listState
            ) {
                items(chapters, key = { it.chapterId!! }) { chapterInfo ->
                    ChapterItem(
                        chapter = chapterInfo,
                        onChapterClick = {
                            viewModel.onItemClick(chapterInfo, naviController)
                        }
                    )
                    Divider(modifier = Modifier.padding(vertical = 4.dp))
                }
                // 加载指示器
                if (isLoading) {
                    item {
                        LoadingCompose()
                    }
                }
                if (!hasMoreData) {
                    item {
                        if (chapters.isEmpty()) {
                            // 空状态
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Default.Book,
                                    contentDescription = "无章节",
                                    modifier = Modifier.size(64.dp),
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                                Text(text = "暂无章节", modifier = Modifier.padding(top = 16.dp))
                            }
                        } else {
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
}



@Composable
fun ChapterListHeader(chapterCount: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "目录 ($chapterCount)",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.SwapVert,
                contentDescription = "排序",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "倒序",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }

    Divider(modifier = Modifier.padding(horizontal = 16.dp))
}