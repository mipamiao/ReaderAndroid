package com.mipa.readerandroid.view.compose.dialog

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Surface
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mipa.readerandroid.R
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.base.dialogcontroller.DialogControllerWithAnim
import com.mipa.readerandroid.model.feature.ChapterInfo
import com.mipa.readerandroid.view.compose.base.AnimatedVisibilityWithCallback
import com.mipa.readerandroid.view.compose.base.LoadingCompose
import com.mipa.readerandroid.view.composedata.DatasShowAllViewModel.Companion.TAG
import com.mipa.readerandroid.view.reader.DirPopupCD

@Composable
fun ReaderDir(
    chapters: List<ChapterInfo>,
    currentChapterIndex: Int,
    onChapterSelect: (ChapterInfo) -> Unit,
    onClose: () -> Unit
) {

    val viewModel = CDMap.get<DirPopupCD>()

    val listState = rememberLazyListState()
    val isLoading = viewModel.isLoading.collectAsState()


    LaunchedEffect(Unit) {
        Log.e("ReaderDir", "LaunchedEffect:", )
        viewModel.loadAlllDatas()
    }


    LaunchedEffect(currentChapterIndex, isLoading.value) {
        if (!isLoading.value && chapters.isNotEmpty() && currentChapterIndex >= 0 && currentChapterIndex < chapters.size) {
            listState.scrollToItem(index = currentChapterIndex, scrollOffset = -80)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)
            )
            .padding(8.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.book_open),
                        contentDescription = "目录",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp).clickable { viewModel.loadAlllDatas() }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "章节列表",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable(onClick = onClose)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "关闭",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            if(isLoading.value){
                LoadingCompose()
            }else {
                if (chapters.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "暂无章节",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp),
                        state = listState,
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(chapters) {
                            ChapterItemView(
                                chapter = it,
                                isCurrent = chapters.indexOf(it) == currentChapterIndex,
                                onClick = { onChapterSelect(it) }
                            )
                        }
                    }
                }
            }


        }
    }
}

/**
 * 章节列表项组件
 */
@Composable
private fun ChapterItemView(
    chapter: ChapterInfo,
    isCurrent: Boolean,
    onClick: () -> Unit
) {
    // 按压状态
    var isPressed by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(
                color = when {
                    isPressed -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    isCurrent -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                    else -> Color.Transparent
                },
                shape = RoundedCornerShape(8.dp)
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = {
                        onClick()
                    }
                )
            }
            .padding(12.dp)
    ) {
        Text(
            text = chapter.title?:"",
            style = when {
                isCurrent -> MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                )
                else -> MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            maxLines = 1
        )
    }
}

@Composable
fun ReaderDirDialog(controller: DialogControllerWithAnim){
    val viewModel  = CDMap.get<DirPopupCD>()

    if (controller.canShow()){
        Popup(alignment = Alignment.CenterStart) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .wrapContentWidth(Alignment.Start)
            ) {
                AnimatedVisibilityWithCallback(
                    name = "ReaderBottomMenuDialog",
                    visible = controller.isShow,
                    enter = slideInHorizontally(animationSpec = tween(durationMillis = 300)) { fullHeight -> -fullHeight },
                    exit = slideOutHorizontally(animationSpec = tween(durationMillis = 300)) { fullHeight -> -fullHeight },
                    onEnterEnd = {controller.endShowing()},
                    onExitEnd = {controller.endDismissing()}
                ) {
                    Surface(
                        modifier = Modifier
                            .wrapContentWidth(Alignment.Start)
                    ) {
                        ReaderDir(
                            chapters = viewModel.datas,
                            currentChapterIndex = viewModel.nowChapterIndex,
                            onChapterSelect = viewModel.onDirItemClick,
                            onClose = {controller.dismiss()}
                        )
                    }
                }
            }
        }
    }
}



