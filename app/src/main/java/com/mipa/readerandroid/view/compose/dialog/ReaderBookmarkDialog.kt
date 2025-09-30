package com.mipa.readerandroid.view.compose.dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.base.dialogcontroller.DialogControllerWithAnim
import com.mipa.readerandroid.model.feature.Bookmark
import com.mipa.readerandroid.view.compose.LocalNavController
import com.mipa.readerandroid.view.compose.base.AnimatedVisibilityWithCallback
import com.mipa.readerandroid.view.compose.base.LoadingCompose
import com.mipa.readerandroid.view.compose.dialogdata.ReaderBookmarkDD


@Composable
fun ReaderBookmark() {
    val viewModel = CDMap.get<ReaderBookmarkDD>()
    val isLoading = viewModel.isLoading.collectAsState()
    val bookmarks = viewModel.datas

    val naviController = LocalNavController.current

    LaunchedEffect(Unit) {
        viewModel.loadAlllDatas()
    }

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp)
            )
            .clickable(onClick = {}),
        contentAlignment = Alignment.TopCenter
    ) {
        Column {
            // 标题栏
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Bookmarks, // 假设存在此图标
                    contentDescription = "书签列表",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "我的书签",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "${bookmarks.size}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = Icons.Filled.Close, // 假设存在此图标
                    contentDescription = "关闭",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            viewModel.dialogController.dismiss()
                        },
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            if (isLoading.value) {
                LoadingCompose()
            } else {
                // 书签列表区域
                if (bookmarks.isEmpty()) {
                    // 空状态
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.BookmarkBorder, // 假设存在此图标
                                contentDescription = "暂无书签",
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "暂无书签",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "阅读时添加书签，随时返回喜欢的内容",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                } else {
                    // 书签列表
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(bookmarks) { bookmark ->
                            BookmarkItem(
                                bookmark = bookmark,
                                onSelect = { viewModel.onItemClick(bookmark, naviController) },
                                onDelete = { viewModel.onDelClick(bookmark) }
                            )
                        }
                    }
                }
            }


        }
    }
}

@Composable
fun BookmarkItem(
    bookmark: Bookmark,
    onSelect: () -> Unit,
    onDelete: () -> Unit
) {
    var isDeleting by remember { mutableStateOf(false) }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(0.dp)
            )
            .padding(bottom = 1.dp)
            .clickable {
                if (!isDeleting) {
                    onSelect()
                }
                isDeleting = false
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 书签图标
        Column(
            modifier = Modifier
                .padding(8.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(4.dp)
                )
                .size(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Bookmarks,
                contentDescription = "书签",
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        // 书签内容
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp, 8.dp, 16.dp, 8.dp)
        ) {
            // 章节标题
            Text(
                text = bookmark.chapterTitle?:"",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            // 备注信息
            if (bookmark.note?.isNotEmpty()==true) {
                Text(
                    text = bookmark.note?:"",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 4.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // 创建时间
            Text(
                text = bookmark.createdAt?:"",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        AnimatedVisibility(
            visible = isDeleting,
            enter = slideInHorizontally(
                animationSpec = tween(durationMillis = 200),
                initialOffsetX = { it / 2 }
            ),
            exit = slideOutHorizontally(
                animationSpec = tween(durationMillis = 200),
                targetOffsetX = { it / 2 }
            )
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        onDelete()
                        isDeleting = false
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.DeleteOutline, // 假设存在此图标
                    contentDescription = "删除",
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }

        // 长按检测区域
        Box(
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.CenterVertically)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            isDeleting = true
                        }
                    )
                }
        )
    }
}


@Composable
fun ReaderBookmarkDialog(controller: DialogControllerWithAnim){

    if(controller.canShow()){
        Popup (
            onDismissRequest = {
                //controller.dismiss()
            },
            alignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f)
                    ,
                contentAlignment = Alignment.TopCenter
            ) {
                AnimatedVisibilityWithCallback(
                    name = "ReaderBookmarkDialog",
                    visible = controller.isShow,
                    enter = slideInVertically(animationSpec = tween(durationMillis = 300)) { fullHeight -> -fullHeight },
                    exit = slideOutVertically(animationSpec = tween(durationMillis = 300)) { fullHeight -> -fullHeight },
                    onEnterEnd = {controller.endShowing()},
                    onExitEnd = {controller.endDismissing()}
                ) {
                    Surface(
                        modifier = Modifier
                            .wrapContentHeight(Alignment.Top)
                    ) {
                        ReaderBookmark()
                    }
                }
            }

        }
    }
}