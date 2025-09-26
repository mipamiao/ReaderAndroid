package com.mipa.readerandroid.view.compose.dialog

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.Surface
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Popup
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.base.dialogcontroller.DialogControllerWithAnim
import com.mipa.readerandroid.view.compose.LocalNavController
import com.mipa.readerandroid.view.compose.base.AnimatedVisibilityWithCallback
import com.mipa.readerandroid.view.reader.ReaderViewCD

@Composable
fun ReaderTopMenu(
    onExitReading: () -> Unit,
    onComment: () -> Unit,
    onListenBook: () -> Unit,
    onAddBookmark: () -> Unit,
    isVisible: Boolean = true
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TopMenuIconButton(
                icon = Icons.Filled.ArrowBack,
                onClick = onExitReading
            )

            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(0.6f) // 右侧区域占总宽度的60%
            ) {
                TopMenuIconButton(
                    icon = Icons.Filled.VolumeUp,
                    onClick = onListenBook,
                    modifier = Modifier.padding(end = 8.dp)
                )

                TopMenuIconButton(
                    icon = Icons.Filled.Comment,
                    onClick = onComment,
                    modifier = Modifier.padding(end = 8.dp)
                )

                TopMenuIconButton(
                    icon = Icons.Filled.BookmarkBorder,
                    onClick = onAddBookmark
                )
            }
        }
    }
}

@Composable
private fun TopMenuIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .size(56.dp)
            .background(
                color = if (isPressed) {
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                } else {
                    Color.Transparent
                },
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(
                onClick = {
                    onClick()
                    Log.e("TAG", "TopMenuIconButton: click", )
                },
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
        contentAlignment = Alignment.Center
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(28.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun preview(){
    MaterialTheme {
        ReaderTopMenu(onExitReading = {}, onComment = {}, onListenBook = {}, onAddBookmark = {})
    }
}

@Composable
fun ReaderTopMenuDialog(controller: DialogControllerWithAnim){
    val viewModel = CDMap.get<ReaderViewCD>()

    val naviController = LocalNavController.current

    if(controller.canShow()){
        Popup (
            onDismissRequest = {
                controller.dismiss()
            },
            alignment = Alignment.TopCenter
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
                            .fillMaxWidth()
                            .wrapContentHeight(Alignment.Top)
                    ) {
                        ReaderTopMenu(
                            onExitReading = {viewModel.onClickBack(naviController)},
                            onComment = {viewModel.onClickComment()},
                            onListenBook = {viewModel.onClickListenBook()},
                            onAddBookmark = {viewModel.onClickAddBookmark()}
                        )
                    }
                }
            }

        }
    }
}

