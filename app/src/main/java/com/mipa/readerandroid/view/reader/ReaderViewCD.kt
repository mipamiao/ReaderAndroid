package com.mipa.readerandroid.view.reader

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.mipa.readerandroid.base.BaseCD
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.base.EffectController.EffectController
import com.mipa.readerandroid.base.dialogcontroller.DialogControllerWithAnim
import com.mipa.readerandroid.model.feature.Book
import com.mipa.readerandroid.model.feature.ChapterInfo
import com.mipa.readerandroid.view.composedata.ChaptersShowViewModel
import com.mipa.readerandroid.view.composedata.base.ChapterCache
import com.mipa.readerandroid.view.composedata.base.ChaptersCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min

class ReaderViewCD: BaseCD() {

    companion object{
        const val TAG = "ReaderViewCD"
    }

    var book: Book? = null
    var bookId: String? = null
    var chapterId: String? = null
    var orderNum: Int? = null


    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    val menuController = DialogControllerWithAnim()
    val dirController = DialogControllerWithAnim()


    val chaptersCache = ChaptersCache()
    var chapterCache = ChapterCache()

    private val _order  = mutableStateOf(0)
    val order: State<Int> = _order

    private val _content = mutableStateOf("")
    val content: State<String> = _content

    private val _title = mutableStateOf("")
    val title: State<String> = _title

    val readerSize =  mutableStateOf(IntSize(0,0))
    val pages = mutableStateOf(emptyList<String>())
    val dirPopupCD = CDMap.get<DirPopupCD>()


    val loadChapterTrigger = EffectController()
    var textMeasure: TextMeasurer? = null
    var density: Density? = null

    var initialPageIndex = 0

    val lineHeight = 30.sp
    val textStyle = TextStyle(
        fontSize = 18.sp,
        fontFamily = FontFamily.Serif,
        lineHeight = lineHeight,
        letterSpacing = 0.5.sp
    )


    fun from(bookId: String?, chapterId: String?){
        this.bookId = bookId
        this.chapterId = chapterId
    }

    fun from(book: Book, bookId: String, order: Int, orderNum: Int) {
        this.book = book
        this.bookId = bookId
        this._order.value = order
        this.orderNum = orderNum

        chaptersCache.orderNum = orderNum
        chaptersCache.bookId = bookId
    }


    @OptIn(ExperimentalFoundationApi::class)
    fun lastPage(pagerState: PagerState, coroutineScope: CoroutineScope) {
        if(clearAllDialog())return
        if(pagerState.settledPage == 0){
            if(lastChapter())
                initialPageIndex = pages.value.size - 1
        }else {
            coroutineScope.launch {
                pagerState.animateScrollToPage(pagerState.settledPage - 1)
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    fun nextPage(pagerState: PagerState, coroutineScope: CoroutineScope) {
        if(clearAllDialog())return
        if (pagerState.settledPage == pagerState.pageCount - 1) {
            if(nextChapter())
                initialPageIndex = 0
        } else {
            coroutineScope.launch {
                pagerState.animateScrollToPage(pagerState.settledPage + 1)
            }
        }
    }

    fun lastChapter(): Boolean {
        if (order.value == 0) {
            ConstValue.showToast("没有上一章啦")
            return false
        }
        _order.value--
        loadChapter(-1)
        return true
    }

    fun nextChapter(): Boolean {
        if (order.value == orderNum!! - 1) {
            ConstValue.showToast("没有下一章啦")
            return false
        }
        _order.value ++
        loadChapter(1)
        return true
    }

    fun loadChapter(isNextOrLast: Int = 0) {
        chapterCache = chaptersCache.get(order.value, onEnd = { chapter ->
            chapter?.let { chapter
                chapter.content?.let {
                    _content.value = it
                    sliceContent()
                    when (isNextOrLast) {
                        1 -> initialPageIndex = 0
                        -1 -> initialPageIndex = pages.value.size - 1
                        else -> initialPageIndex = 0
                    }
                }
                chapter.chapterInfo?.title?.let {
                    _title.value = it
                }

            }
        })
    }

    fun sliceContent(){
        val lineHeightPx = density?.let { with(it){lineHeight.toPx()} }?:0f
        val constraints = Constraints(
            maxWidth = readerSize.value.width, // 最大宽度（像素）
            maxHeight = Int.MAX_VALUE
        )
        val res = textMeasure?.measure(
            text = AnnotatedString(content.value),
            style = textStyle,
            constraints = constraints
        )?.let {
            sliceText(
                content.value,
                it,
                readerSize.value.height,
                (lineHeightPx + 0.5f).toInt()
            )
        }
        res?.let { pages.value = res }
        Log.e(TAG, "sliceContent: pages-size: ${pages.value.size}")
        //Log.e(TAG, "sliceContent: pagerState: ${pages.value.size}")
    }


    fun switchMenu(){

        menuController.switch()
    }

    fun openMenu(){
        if(clearAllDialog())return
        menuController.show()
    }


    fun onClickFontStyleItem() {

    }

    fun onClickFontSizeItem() {

    }

    fun onClickChapterListItem() {
        menuController.dismiss()
        book?.let {
            dirPopupCD.from(it, order.value) { info ->
                onClickDirItem(info)
            }
        }
        dirController.show()
    }

    fun onClickBookmarkItem() {

    }

    fun onClickBack(naviController: NavHostController){
        naviController.popBackStack()
    }

    fun onClickListenBook(){

    }

    fun onClickComment(){

    }

    fun onClickAddBookmark(){

    }

    fun onClickDirItem(chapterInfo: ChapterInfo){
        clearAllDialog()
        chapterInfo.order?.let {
            _order.value = it
            loadChapter(1)
        }
    }

    fun clearAllDialog(): Boolean {
        return menuController.dismiss() ||
                dirController.dismiss()
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
