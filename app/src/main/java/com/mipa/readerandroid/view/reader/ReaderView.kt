package com.mipa.readerandroid.view.reader

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.view.compose.LocalNavController
import com.mipa.readerandroid.view.compose.base.LoadingCompose
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderScreen() {
    val viewModel = CDMap.get<ReaderViewCD>()
    val chapter by viewModel.chapter.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // 控制顶部和底部栏的显示
    val showControls by remember { mutableStateOf(true) }

    val naviController = LocalNavController.current

    // 加载章节数据
    LaunchedEffect(chapter) {
        viewModel.getData()
    }

//    // 翻页状态
//    val currentPageContent by remember {
//        derivedStateOf { viewModel.getCurrentPageContent() }
//    }

    Scaffold(
        topBar = {
            if (showControls) {
                TopAppBar(
                    title = {
                        Column {
                            Text(chapter.chapterInfo?.bookId?:"default", maxLines = 1, fontSize = 16.sp)
                            Text(chapter.chapterInfo?.title?: "", maxLines = 1, fontSize = 14.sp)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { naviController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0x88000000),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            }
        },
        bottomBar = {
            if (showControls) {
                BottomAppBar(
                    content = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = {  }) {
                                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "上一页")
                            }
                            Text(
                                text = "50",
                                color = Color.White
                            )
                            IconButton(onClick = {  }) {
                                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "下一页")
                            }
                        }
                    },
                    containerColor = Color(0x88000000)
                )
            }
        }
    ) {
        // 阅读内容区域
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(Color(0xFFF5F5DC)) // 米黄色背景，适合阅读
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { offset ->
                            // 根据点击位置判断翻页或切换控制栏
                            val screenWidth = size.width
                            if (offset.x < screenWidth / 3) {
                                //viewModel.previousPage()
                            } else if (offset.x > screenWidth * 2 / 3) {
                                //viewModel.nextPage()
                            }
                        }
                    )
                }
        ) {
            if (isLoading) {
                LoadingCompose()
            } else {
                // 内容显示
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = chapter.chapterInfo?.title ?: "",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp)
                    )

                    Text(
                        text = chapter.content?:"dcasdcasdcasdcasdcasdcsdcasdcasdcas",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = FontFamily.Serif,
                            lineHeight = 30.sp,
                            letterSpacing = 0.5.sp
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
        }
    }
}

fun sliceText(text: String, textLayoutResult: TextLayoutResult, maxHeight: Int): List<String> {
    val lineHeight = textLayoutResult.getLineBottom(0) - textLayoutResult.getLineTop(0)
    val maxLineCount = (maxHeight / lineHeight - 0.5f).toInt()
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