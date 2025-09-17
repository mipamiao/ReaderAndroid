package com.mipa.readerandroid.view.compose


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.view.compose.base.ChapterItem
import com.mipa.readerandroid.view.compose.base.LoadingCompose
import com.mipa.readerandroid.view.composedata.ChapterListPageCD
import com.mipa.readerandroid.view.reader.ReaderViewCD


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterListScreen() {
    val viewModel = ChapterListPageCD
    val chapters = viewModel.chapters
    val isLoading by viewModel.isLoading.collectAsState()
    val currentBook by viewModel.book.collectAsState()

    var naviControllrt = LocalNavController.current

    // 加载章节数据
    LaunchedEffect(currentBook) {
        viewModel.loadMoreData()
    }
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column {
            ChapterListHeader(chapters.size)
            if (isLoading) {
                LoadingCompose()
            } else if (chapters.isEmpty()) {
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
                // 章节列表
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(chapters) {chapter->
                        ChapterItem(
                            chapter = chapter,
                            onChapterClick = {
                                ReaderViewCD.bookId = currentBook.bookId
                                ReaderViewCD.chapterId = chapter.chapterId
                                naviControllrt.navigate(ConstValue.ROUTER_READER_PAGE) {
                                    launchSingleTop = true
                                }
                            }
                        )
                        Divider(modifier = Modifier.padding(vertical = 4.dp))
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