package com.mipa.readerandroid.view.compose.base

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun LoadingCompose(modifier: Modifier = Modifier) {
    Column (
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        Text(
            text = "加载中...",
            modifier = Modifier
                .padding(start = 8.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun LoadingCompose2(
    modifier: Modifier = Modifier,
    enableHeightAnimation: Boolean = true, // 新增参数，控制是否启用高度动画
    animationDuration: Int = 500 // 新增参数，控制动画持续时间（毫秒）
) {
    // 计算组件的预期高度（基于CircularProgressIndicator的默认大小）
    val targetHeight = 40.dp // CircularProgressIndicator的默认大小约为40dp

    // 创建高度动画状态
    val height by animateDpAsState(
        targetValue = if (enableHeightAnimation) targetHeight else targetHeight, // 如果启用动画，则从0过渡到targetHeight
        animationSpec = tween(
            durationMillis = if (enableHeightAnimation) animationDuration else 0, // 控制动画持续时间
            easing = FastOutSlowInEasing // 使用常见的缓动函数
        ),
        label = "LoadingHeightAnimation"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(height) // 应用动画高度
            .wrapContentHeight() // 确保内容不会被裁剪
    ) {
        // 保持原有的内容和布局
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically // 垂直居中内容
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp) // 设置进度条大小
            )
            Text(
                text = "加载中...",
                modifier = Modifier
                    .padding(start = 8.dp)
            )
        }
    }
}