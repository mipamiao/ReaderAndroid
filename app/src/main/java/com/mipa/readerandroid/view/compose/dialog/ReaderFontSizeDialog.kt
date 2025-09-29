package com.mipa.readerandroid.view.compose.dialog


import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mipa.readerandroid.R
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.base.dialogcontroller.DialogController
import com.mipa.readerandroid.base.dialogcontroller.DialogControllerWithAnim
import com.mipa.readerandroid.view.compose.base.AnimatedVisibilityWithCallback
import com.mipa.readerandroid.view.reader.ReaderViewCD
import kotlin.math.roundToInt


@Composable
fun ReaderFontSize(
    onDismiss: () -> Unit,
    initialFontSize: Int = 18,
    onFontSizeChanged: (Int) -> Unit
) {


    // 内部字号状态
    var fontSize by remember { mutableStateOf(initialFontSize) }


    // 当内部字号变化时通知外部
    fun handleFontSizeChange(newSize: Int) {
        fontSize = newSize
        onFontSizeChanged(newSize)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.background)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .clickable(onClick = {}),
        contentAlignment = Alignment.BottomCenter
    ) {
        // 标题栏
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    ,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.font_size), // 假设存在此图标
                    contentDescription = "字号调整",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "字号调整",
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

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {

                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "小",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Text(
                            text = "$fontSize",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = "大",
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Slider(
                        value = fontSize.toFloat(),
                        onValueChange = { newValue ->
                            handleFontSizeChange(newValue.roundToInt())
                        },
                        valueRange = 12f..42f,
                        steps = 29,
                        modifier = Modifier.fillMaxWidth(),
                        colors = androidx.compose.material3.SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.primary,
                            activeTrackColor = MaterialTheme.colorScheme.primary,
                            inactiveTrackColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf(24, 26, 28, 30, 32).forEach {size ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = if (fontSize == size) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.surfaceVariant
                                    },
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (fontSize == size) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.outlineVariant
                                    },
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    handleFontSizeChange(size)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = size.toString(),
                                fontSize = (size - 2).sp,
                                color = if (fontSize == size) {
                                    MaterialTheme.colorScheme.onPrimary
                                } else {
                                    MaterialTheme.colorScheme.onBackground
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ReaderFontSizeDialog(controller: DialogControllerWithAnim){

     val viewModel = CDMap.get<ReaderViewCD>()

    val style = viewModel.textStyle.value

    if(controller.canShow()){
        Popup (
            onDismissRequest = {
                //controller.dismiss()
            },
            alignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(Alignment.Bottom)
            ) {
                AnimatedVisibilityWithCallback(
                    name = "ReaderFontSizeDialog",
                    visible = controller.isShow,
                    enter = slideInVertically(animationSpec = tween(durationMillis = 300)) { fullHeight -> fullHeight }, // 从底部进入
                    exit = slideOutVertically(animationSpec = tween(durationMillis = 300)) { fullHeight -> fullHeight }, // 向底部退出
                    onEnterEnd = {controller.endShowing()},
                    onExitEnd = {controller.endDismissing()}
                ) {
                    Surface(
                        modifier = Modifier
                            .wrapContentHeight(Alignment.Bottom)
                    ) {
                        ReaderFontSize(
                            onDismiss = { controller.dismiss() },
                            initialFontSize = style.fontSize.value.toInt(),
                            onFontSizeChanged = { value ->
                                viewModel.setTextStyle(
                                    TextStyle(
                                        lineHeight = (value + 10).sp,
                                        fontSize = value.sp,
                                        fontFamily = style.fontFamily,
                                        letterSpacing = style.letterSpacing
                                    )
                                )
                            }
                        )
                    }
                }
            }

        }
    }
}