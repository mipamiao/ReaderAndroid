package com.mipa.readerandroid.view.compose.dialog


import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.base.dialogcontroller.DialogControllerWithAnim
import com.mipa.readerandroid.view.compose.base.AnimatedVisibilityWithCallback
import com.mipa.readerandroid.view.reader.ReaderViewCD

@Preview
@Composable
fun ReadingBottomMenuPreview() {
    MaterialTheme {
        ReadingBottomMenu(
            onFontSelect = { },
            onFontSizeSelect = { },
            onChapterListSelect = { },
            onBookmarkSelect = { }
        )
    }
}

@Composable
fun ReadingBottomMenu(
    onFontSelect: () -> Unit,
    onFontSizeSelect: () -> Unit,
    onChapterListSelect: () -> Unit,
    onBookmarkSelect: () -> Unit
) {
    // 动画颜色状态
    val backgroundColor by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.surface,
        animationSpec = tween(durationMillis = 300)
    )

    // 主容器 - 减少padding
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .wrapContentHeight(Alignment.Bottom) // 关键：内容贴底
            .padding(4.dp) // 从8.dp减少到4.dp
    ) {
        // 顶部指示器 - 保留但可以考虑移除
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(3.dp) // 从4.dp减少到3.dp
                    .background(MaterialTheme.colorScheme.outlineVariant)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(2.dp)
                    )
            )
        }

        // 菜单选项区域 - 减少整体padding
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp), // 从16.dp减少到8.dp
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 字体选项
            MenuItem(
                icon = Icons.Filled.TextFields,
                label = "字体",
                onClick = onFontSelect
            )

            // 字号选项
            MenuItem(
                icon = Icons.Filled.FormatSize,
                label = "字号",
                onClick = onFontSizeSelect
            )

            // 目录选项
            MenuItem(
                icon = Icons.Filled.List,
                label = "目录",
                onClick = onChapterListSelect
            )

            // 书签选项
            MenuItem(
                icon = Icons.Filled.Bookmark,
                label = "书签",
                onClick = onBookmarkSelect
            )
        }
    }
}

/**
 * 菜单项组件 - 图标在上，文字在下 - 紧凑版
 */
@Composable
private fun MenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }

    // 使用状态驱动的背景色
    val backgroundColor = if (isPressed) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
    } else {
        Color.Transparent
    }

    Column(
        modifier = Modifier
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    }
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 图标区域 - 缩小图标容器
        Box(
            modifier = Modifier
                .size(48.dp) // 从56.dp减少到48.dp
                .background(backgroundColor, shape = RoundedCornerShape(10.dp)) // 圆角从12.dp减少到10.dp
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp) // 从24.dp减少到20.dp
            )
        }
        // 文字标签 - 减少间距并调整文字大小
        Spacer(modifier = Modifier.height(4.dp)) // 从6.dp减少到4.dp
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), // 添加更小的字体大小
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 4.dp) // 从8.dp减少到4.dp
        )
    }
}


@Composable
fun ReaderBottomMenuDialog(controller: DialogControllerWithAnim){
    val viewModel = CDMap.get<ReaderViewCD>()

    if(controller.canShow()){
        Popup (
            onDismissRequest = {
                controller.dismiss()
            },
            alignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(Alignment.Bottom) // 内容贴底
            ) {
                AnimatedVisibilityWithCallback(
                    name = "ReaderBottomMenuDialog",
                    visible = controller.isShow,
                    enter = slideInVertically(animationSpec = tween(durationMillis = 300)) { fullHeight ->fullHeight }, // 从底部进入
                    exit = slideOutVertically(animationSpec = tween(durationMillis = 300)) { fullHeight -> fullHeight }, // 向底部退出
                    onEnterEnd = {controller.endShowing()},
                    onExitEnd = {controller.endDismissing()}
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(Alignment.Bottom) // 关键：内容贴底
                    ) {
                        ReadingBottomMenu(
                            onBookmarkSelect = { viewModel.onClickBookmarkItem() },
                            onChapterListSelect = { viewModel.onClickChapterListItem() },
                            onFontSelect = { viewModel.onClickFontStyleItem() },
                            onFontSizeSelect = { viewModel.onClickFontSizeItem() },
                        )
                    }
                }
            }

        }
    }
}

