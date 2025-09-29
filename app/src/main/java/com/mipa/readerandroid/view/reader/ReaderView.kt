package com.mipa.readerandroid.view.reader

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.view.compose.LocalNavController
import com.mipa.readerandroid.view.compose.base.LoadingCompose
import com.mipa.readerandroid.view.compose.dialog.ReaderBottomMenuDialog
import com.mipa.readerandroid.view.compose.dialog.ReaderDirDialog
import com.mipa.readerandroid.view.compose.dialog.ReaderFontSize
import com.mipa.readerandroid.view.compose.dialog.ReaderFontSizeDialog
import com.mipa.readerandroid.view.compose.dialog.ReaderTopMenuDialog

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ReaderScreen() {
    val TAG = "ReaderScreen"
    val viewModel = CDMap.get<ReaderViewCD>()
    val chapterCache = viewModel.chapterCache
    val isLoading by chapterCache.isLoading.collectAsState()

    val pages = viewModel.pages
    Log.e(TAG, "ReaderScreen: pages-size: ${pages.value.size}")

    viewModel.textMeasure = rememberTextMeasurer()
    viewModel.density = LocalDensity.current

    val title = viewModel.title


    val naviController = LocalNavController.current
    val coroutineScope = rememberCoroutineScope()

    // 加载章节数据
    LaunchedEffect(Unit) {
        viewModel.loadChapter()
    }

//    LaunchedEffect(viewModel.readerSize.value, text.value) {
//        delay(100) // 等待100ms，避免频繁更新
//        Log.e(TAG, "稳定尺寸: ${rawSize ?: "null"}")
//    }


    ReaderBottomMenuDialog(viewModel.menuController)
    ReaderTopMenuDialog(viewModel.menuController)
    ReaderDirDialog(viewModel.dirController)
    ReaderFontSizeDialog(viewModel.fontSizeController)

    val pagerState = rememberPagerState(pageCount = {pages.value.size}) // 总页数
    LaunchedEffect(pages.value) {
        if (viewModel.initialPageIndex >= 0) pagerState.scrollToPage(viewModel.initialPageIndex)
    }

    Column(modifier = Modifier
        .fillMaxSize()) {
        Text(
            text = title.value,
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
                    viewModel.readerSize.value = size
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { offset ->
                            val screenWidth = size.width
                            if (offset.x < screenWidth / 3) {
                                viewModel.lastPage(pagerState, coroutineScope)
                            } else if (offset.x > screenWidth * 2 / 3) {
                                viewModel.nextPage(pagerState, coroutineScope)
                            } else {
                                viewModel.openMenu()
                            }
                        }
                    )
                }
        ) {
            if(isLoading){
                LoadingCompose()
            }else {
                HorizontalPager(state = pagerState, userScrollEnabled = false) { page ->
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = pages.value[page],
                            style = viewModel.textStyle.value,
                            modifier = Modifier
                        )
                    }
                }
            }
        }
    }
}
