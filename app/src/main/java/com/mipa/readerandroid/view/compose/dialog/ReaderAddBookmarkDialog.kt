package com.mipa.readerandroid.view.compose.dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup
import com.mipa.readerandroid.R
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.base.dialogcontroller.DialogControllerWithAnim
import com.mipa.readerandroid.view.compose.base.AnimatedVisibilityWithCallback
import com.mipa.readerandroid.view.compose.base.LoadingCompose
import com.mipa.readerandroid.view.reader.BookmarkPopupCD


@Composable
fun ReaderAddBookmark(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
    chapterTitle: String = "当前章节",
    note: String = ""
) {
    val viewModel = CDMap.get<BookmarkPopupCD>()
    val isLoading = viewModel.isLoading.collectAsState()

    // 备注文本状态
    var noteText by remember { mutableStateOf(TextFieldValue(note)) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }


    LaunchedEffect(isLoading.value) {
        noteText = TextFieldValue(viewModel.getCurrentNote())
    }

    // 保存书签备注
    fun handleSave() {
        onSave(noteText.text)
        noteText = TextFieldValue("") // 清空输入
        onDismiss()
    }
    Box(
        modifier = Modifier
            .wrapContentHeight()
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
                    imageVector = Icons.Filled.BookmarkBorder,
                    contentDescription = "添加书签",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "添加书签",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "关闭",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            onDismiss()
                        },
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            // 内容区域
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                if (isLoading.value) {
                    LoadingCompose()
                } else {
                    // 章节信息显示
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outlineVariant,
                                shape = RoundedCornerShape(8.dp)
                            )
                    ) {
                        Text(
                            text = "当前章节",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            text = chapterTitle,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // 备注输入区域
                    Column {
                        Text(
                            text = "备注",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        BasicTextField(
                            value = noteText,
                            onValueChange = { noteText = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .background(MaterialTheme.colorScheme.surface)
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.outline,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(12.dp),keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Text
                            ),
                            textStyle = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 16.sp
                            ),
                            maxLines = 5,
                            singleLine = false
                        ) {
                                innerTextField ->
                            Box(modifier = Modifier.padding(4.dp)) {
                                if (noteText.text.isEmpty()) {
                                    Text(
                                        text = "输入备注信息（选填）",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                }
                                innerTextField()
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // 底部按钮区域
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        // 取消按钮
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .clickable {
                                    onDismiss()
                                }
                                .padding(12.dp, 8.dp)
                        ) {
                            Text(
                                text = "取消",
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        // 保存按钮
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    handleSave()
                                }
                                .padding(16.dp, 8.dp)
                        ) {
                            Text(
                                text = "保存",
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun ReaderAddBookmarkDialog(controller: DialogControllerWithAnim){
    val viewModel = CDMap.get<BookmarkPopupCD>()

    if(controller.canShow()){
        Dialog (
            onDismissRequest = {
                controller.dismiss()
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false, // 避免键盘被挤掉
                decorFitsSystemWindows = false   // 让系统自动调整
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(Alignment.Top)
            ) {
                AnimatedVisibilityWithCallback(
                    name = "ReaderBottomMenuDialog",
                    visible = controller.isShow,
                    enter = slideInVertically(animationSpec = tween(durationMillis = 300)) { fullHeight -> -fullHeight }, // 从底部进入
                    exit = slideOutVertically(animationSpec = tween(durationMillis = 300)) { fullHeight -> -fullHeight }, // 向底部退出
                    onEnterEnd = {controller.endShowing()},
                    onExitEnd = {controller.endDismissing()}
                ) {
                    Surface(
                        modifier = Modifier
                            .wrapContentHeight(Alignment.Top)
                    ) {
                        ReaderAddBookmark(
                            onDismiss = {controller.dismiss()},
                            onSave = viewModel.onSaveNoteClick,
                            chapterTitle = viewModel.nowChapterTitle,
                            note = viewModel.getCurrentNote()
                        )
                    }
                }
            }

        }
    }
}