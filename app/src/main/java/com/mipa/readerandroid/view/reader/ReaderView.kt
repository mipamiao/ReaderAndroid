package com.mipa.readerandroid.view.reader

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.min

//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun showText(text: String) {
//    val density = LocalDensity.current
//    val widthPx = with(density){width.dp.toPx()}
//    val heightPx = with(density){height.dp.toPx()}
//
//    var constraints by remember { mutableStateOf<Constraints?>(null) }
//
//    LocalContext.current.resources.displayMetrics.let { displayMetrics ->
//        constraints = Constraints(
//            maxWidth = displayMetrics
//                .widthPixels
//                .minus(
//                    with(LocalDensity.current) {
//                        (paddingValues.calculateStartPadding(LayoutDirection.Ltr) + paddingValues.calculateEndPadding(
//                            LayoutDirection.Ltr
//                        ))
//                            .toPx()
//                    }.toInt()
//                ),
//            maxHeight = displayMetrics
//                .heightPixels
//                .minus(
//                    with(LocalDensity.current) {
//                        (paddingValues.calculateTopPadding() + paddingValues.calculateBottomPadding() + 10.dp)
//                            .toPx()
//                    }.toInt()
//                ),
//        )
//    }
//
//    val textMeasurer = rememberTextMeasurer()
//    val fontSize = 16.sp
//    val textLayoutResult = textMeasurer.measure(
//        text = AnnotatedString(text),
//        style = TextStyle(fontSize = fontSize),
//        constraints = Constraints(maxWidth = widthPx.toInt())
//    )
//    val texts = sliceText(text, textLayoutResult, heightPx.toInt())
//    val index by remember { mutableStateOf(0) }
//    val pagerState = rememberPagerState(initialPage = 0, pageCount = {texts.size})
//
//    HorizontalPager(
//        state = pagerState
//    ) { page ->
//        // page 是当前要绘制的页号
//        Box(modifier = Modifier.clickable {
//            CoroutineScope(Dispatchers.Main).launch {
//                pagerState.animateScrollToPage(
//                    pagerState.currentPage + 1
//                )
//            }
//        }) {
//            cons
//            Text(texts.get(page))
//        }
//
//    }
//
//}

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