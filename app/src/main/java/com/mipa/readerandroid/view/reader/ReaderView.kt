package com.mipa.readerandroid.view.reader

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.view.compose.LocalNavController
import com.mipa.readerandroid.view.compose.base.LoadingCompose
import com.mipa.readerandroid.view.compose.dialog.ReaderBottomMenuDialog
import com.mipa.readerandroid.view.compose.dialog.ReaderTopMenuDialog
import kotlinx.coroutines.delay
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ReaderScreen() {
    val TAG = "ReaderScreen"
    val viewModel = CDMap.get<ReaderViewCD>()
    val chapter by viewModel.chapter.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val pages = remember { mutableStateOf(emptyList<String>()) }
    Log.e(TAG, "ReaderScreen: pages-size: ${pages.value.size}", )


    var rawSize by remember { mutableStateOf<IntSize?>(null) }
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val lineHeight = 30.sp
    val lineHeightPx = with(density){lineHeight.toPx()}
    val text = chapter.content
    val textStyle = TextStyle(
        fontSize = 18.sp,
        fontFamily = FontFamily.Serif,
        lineHeight = lineHeight,
        letterSpacing = 0.5.sp
    )

    val naviController = LocalNavController.current

    // 加载章节数据
    LaunchedEffect(chapter) {
        viewModel.getData()
    }
    LaunchedEffect(rawSize, chapter) {
        delay(100) // 等待100ms，避免频繁更新
        Log.e(TAG,"稳定尺寸: ${rawSize?:"null"}")
        pages.value = rawSize?.let { rawSize ->
            val constraints = Constraints(
                maxWidth = rawSize.width, // 最大宽度（像素）
                maxHeight = Int.MAX_VALUE
            )
            text?.let {
                sliceText(
                    it,
                    textMeasurer.measure(
                        text = AnnotatedString(it),
                        style = textStyle,
                        constraints = constraints
                    ),
                    rawSize.height,
                    (lineHeightPx + 0.5f).toInt()
                )
            }
        } ?: emptyList()
    }



    ReaderBottomMenuDialog(viewModel.menuController)
    ReaderTopMenuDialog(viewModel.menuController)

    val pagerState = rememberPagerState(pageCount = {pages.value.size}) // 总页数

    Column(modifier = Modifier
        .fillMaxSize()) {
        Text(
            text = chapter.chapterInfo?.title ?: "",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .padding(bottom = 24.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5DC)) // 米黄色背景，适合阅读
                .padding(horizontal = 8.dp)
                .onSizeChanged { size ->
                    Log.e(TAG, "变化尺寸: $size")
                    rawSize = size
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { offset ->
                            val screenWidth = size.width
                            if (offset.x < screenWidth / 3) {
                                viewModel.lastPage()
                            } else if (offset.x > screenWidth * 2 / 3) {
                                viewModel.nextPage()
                            } else {
                                viewModel.switchMenu()
                            }
                        }
                    )
                }
        ) {
            HorizontalPager(state = pagerState) { page ->
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (false) {
                        LoadingCompose()
                    } else {
                        Text(
                            text = pages.value[page],
                            style = textStyle,
                            modifier = Modifier
                        )
                    }
                }
            }
        }

    }
}

fun sliceText(text: String, textLayoutResult: TextLayoutResult, maxHeight: Int, lineHeightPx: Int): List<String> {
    val maxLineCount =  maxHeight/lineHeightPx
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