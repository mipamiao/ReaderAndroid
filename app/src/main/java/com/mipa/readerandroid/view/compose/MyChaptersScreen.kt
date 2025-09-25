package com.mipa.readerandroid.view.compose


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mipa.readerandroid.R
import com.mipa.readerandroid.model.feature.Chapter
import com.mipa.readerandroid.model.feature.ChapterInfo
import com.mipa.readerandroid.view.composedata.MyChaptersPageCD
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.view.compose.base.LoadingCompose

//todo 从writerView返回没必要重新请求章节列表
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyChaptersPageScreen() {
    val viewModel = CDMap.get<MyChaptersPageCD>()
    val chapters = viewModel.datas
    val isLoading by viewModel.isLoading.collectAsState()
    val hasMoreData by viewModel.hasMoreData.collectAsState()
    val currentBook by viewModel.book.collectAsState()

    val naviController = LocalNavController.current

    // 加载章节数据
    LaunchedEffect(currentBook) {
        viewModel.refresh()
    }

    val listState = rememberLazyListState()

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

        // 章节列表
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            state = listState
        ) {
            items(chapters, key = { it.chapterId!! }) { chapterInfo ->
                AuthorChapterItem(
                    chapterinfo = chapterInfo,
                    onChapterClick = { viewModel.onEditClick(chapterInfo, naviController) },
                    onEditClick = { viewModel.onEditClick(chapterInfo, naviController) },
                    onDeleteClick = { viewModel.onDeleteClick(chapterInfo) }
                )
                Spacer(modifier = Modifier.height(8.dp))
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
                            Text(
                                text = "暂无章节，点击右上角添加",
                                modifier = Modifier.padding(top = 16.dp)
                            )
                            // 快捷添加按钮
                            Button(
                                onClick = { viewModel.onAddClick(naviController) },
                                modifier = Modifier.padding(top = 16.dp)
                            ) {
                                Text("立即添加第一章")
                            }
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

@Composable
fun AuthorChapterItem(
    chapterinfo: ChapterInfo,
    onChapterClick: (ChapterInfo) -> Unit,
    onEditClick: (ChapterInfo) -> Unit,
    onDeleteClick: (ChapterInfo) -> Unit
) {
    // 展开/收起状态
    val expanded = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onChapterClick(chapterinfo)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 章节标题和序号
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // 章节序号背景
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${chapterinfo.order}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    // 章节标题
                    Text(
                        text = chapterinfo.title ?: "未命名章节",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1, // 限制一行
                        overflow = TextOverflow.Ellipsis,
                        //modifier = Modifier.weight(1f)
                    )
                }

                // 展开/收起箭头
                IconButton(
                    onClick = { expanded.value = !expanded.value },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = if (expanded.value) "收起" else "展开",
                        modifier = Modifier.rotate(if (expanded.value) 180f else 0f)
                    )
                }
            }

            // 章节信息
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "${chapterinfo.wordCount}字",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "更新于 ${chapterinfo.updatedAt}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }

            // 操作按钮 - 展开时显示
            if (expanded.value) {
                Divider(modifier = Modifier.padding(vertical = 12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 修改按钮
                    TextButton(
                        onClick = {
                            expanded.value = false
                            onEditClick(chapterinfo)
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "修改", modifier = Modifier.size(16.dp))
                        Text("修改", modifier = Modifier.padding(start = 4.dp))
                    }

                    // 删除按钮
                    TextButton(
                        onClick = {
                            expanded.value = false
                            onDeleteClick(chapterinfo)
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "删除", modifier = Modifier.size(16.dp))
                        Text("删除", modifier = Modifier.padding(start = 4.dp))
                    }
                }
            }
        }
    }
}