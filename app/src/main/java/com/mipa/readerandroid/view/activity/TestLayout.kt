package com.mipa.readerandroid.view.activity

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.min
import kotlin.math.roundToInt

@Preview
@Composable
fun TestScreen() {
    val content = remember { mutableStateOf("") }
    val style = TextStyle(
        fontSize = 18.sp,
        fontFamily = FontFamily.Serif,
        // lineHeight = 30.sp,
        letterSpacing = 0.5.sp
    )
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val size = 200.dp
    val sizePx = with(density) { size.toPx() }
    val lineHeightPx = with(density){24.sp.toPx()}
    val TAG = "Test"
    Log.e(TAG, "TestScreen: lineHeightPx =  ${lineHeightPx}", )

    Column(
        modifier = Modifier.fillMaxSize(), // 关键点 1：让 Column 占满全屏
        verticalArrangement = Arrangement.Center, // 关键点 2：垂直居中
        horizontalAlignment = Alignment.CenterHorizontally // 关键点 3：水平居中
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .padding(0.dp)
                .background(Color(0xFFFF0000)),
            contentAlignment = Alignment.Center // 关键点 4：Box 内容居中
        ) {
            Text(
                text = content.value,
                style = style,
                onTextLayout = {layout->
                    val lineHeight = layout.getLineBottom(0) - layout.getLineTop(0)
                    Log.e("Test", "onTextLayout: lineHeight: $lineHeight", )
                },
                modifier = Modifier
                    .size(size)
                    .padding(0.dp)
                    .background(Color(0xFF00FF00))
            )
        }

        Box(
            modifier = Modifier.size(size),
            contentAlignment = Alignment.Center // 关键点 4：Box 内容居中
        ) {
            TextField(
                value = content.value,
                onValueChange = {
                    content.value = it
                    textMeasurer.measure(
                        text = AnnotatedString(it),
                        style = style,
                        constraints = Constraints(maxWidth = sizePx.toInt())
                    ).let {
                        val pages = sliceText1(content.value, it, sizePx.toInt())
                        Log.d("Test", "Line count: ${it.lineCount}  Page count: ${pages.size}")
                    }
                },
                placeholder = { Text("输入章节内容") },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp),
                textStyle = style,
                maxLines = Int.MAX_VALUE,

            )
        }

    }
}

fun sliceText1(text: String, textLayoutResult: TextLayoutResult, maxHeight: Int): List<String> {
    val lineHeight = textLayoutResult.getLineBottom(0) - textLayoutResult.getLineTop(0)
    Log.e("Test", "sliceText1: lineHeight: $lineHeight", )
    if(textLayoutResult.lineCount>1){
        val lineHeight2 = textLayoutResult.getLineBottom(1) - textLayoutResult.getLineTop(0)
        Log.e("Test", "sliceText1: lineHeight22222: $lineHeight2", )
    }

    val maxLineCount = (maxHeight / lineHeight).toInt()
    var startLine = 0
    val texts: MutableList<String> = mutableListOf()
    while (startLine < textLayoutResult.lineCount) {
        val endLine = min(startLine + maxLineCount, textLayoutResult.lineCount)
        texts.add(
            text.substring(
                textLayoutResult.getLineStart(startLine),
                textLayoutResult.getLineEnd(endLine - 1)
            )
        )
        startLine = endLine
    }
    return texts
}