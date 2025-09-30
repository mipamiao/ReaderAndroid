package com.mipa.readerandroid.view.reader

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
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
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.mipa.readerandroid.base.BaseCD
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.base.EffectController.EffectController
import com.mipa.readerandroid.base.dialogcontroller.DialogControllerWithAnim
import com.mipa.readerandroid.model.dto.BookmarkRequestDto
import com.mipa.readerandroid.model.feature.Book
import com.mipa.readerandroid.model.feature.Bookmark
import com.mipa.readerandroid.model.feature.ChapterInfo
import com.mipa.readerandroid.service.BookmarkService
import com.mipa.readerandroid.service.CustomedSettingService
import com.mipa.readerandroid.view.composedata.base.ChapterCache
import com.mipa.readerandroid.view.composedata.base.ChaptersCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    val fontSizeController = DialogControllerWithAnim()
    val addBookmarkController = DialogControllerWithAnim()
    val bookmarkController = DialogControllerWithAnim()


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
    val bookmarkPopupCD = CDMap.get<BookmarkPopupCD>()


    val loadChapterTrigger = EffectController()
    var textMeasure: TextMeasurer? = null
    var density: Density? = null

    var initialPageIndex = 0

    val lineHeight = 28.sp
    val fontSize = 18
    val _textStyle = mutableStateOf(TextStyle(
        fontSize = 18.sp,
        fontFamily = FontFamily.Serif,
        lineHeight = lineHeight,
        letterSpacing = 0.5.sp
    ))
    val textStyle: State<TextStyle>  = _textStyle

    init {
        loadReaderSetting()
    }


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

        initialBookmark()
        initialDir()
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
                    chapterId = chapter.chapterInfo?.chapterId
                    sliceContent()
                    when (isNextOrLast) {
                        1 -> initialPageIndex = 0
                        -1 -> initialPageIndex = pages.value.size - 1
                        else -> initialPageIndex = -1
                    }
                }
                chapter.chapterInfo?.title?.let {
                    _title.value = it
                }

            }
        })
    }

    fun flushPages(){
        sliceContent()
        initialPageIndex = -1
    }

    fun sliceContent(){
        val lineHeightPx = density?.let { with(it){textStyle.value.lineHeight.toPx()} }?:0f
        val constraints = Constraints(
            maxWidth = readerSize.value.width, // 最大宽度（像素）
            maxHeight = Int.MAX_VALUE
        )
        val res = textMeasure?.measure(
            text = AnnotatedString(content.value),
            style = textStyle.value,
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


    fun openMenu(){
        if(clearAllDialog())return
        menuController.show()
    }


    fun onClickFontStyleItem() {

    }

    fun onClickFontSizeItem() {
        clearAllDialog()
        fontSizeController.show()
    }

    fun onClickChapterListItem() {
        menuController.dismiss()

        dirController.show()
    }

    fun onClickOpenBookmarkItem() {
        menuController.dismiss()
        bookmarkController.show()
    }

    fun onClickBack(naviController: NavHostController){
        naviController.popBackStack()
    }

    fun onClickListenBook(){

    }

    fun onClickComment(){

    }

    fun onClickAddBookmark() {
        menuController.dismiss()
        bookmarkPopupCD.nowChapterTitle = title.value
        bookmarkPopupCD.nowChapterIndex = order.value
        addBookmarkController.show()
    }

    fun onClickDirItem(chapterInfo: ChapterInfo){
        clearAllDialog()
        chapterInfo.order?.let {
            _order.value = it
            loadChapter(1)
        }
    }

    fun onClickBookmarkItem(bookmark: Bookmark){
        clearAllDialog()
        bookmark.order?.let {
            _order.value = it
            loadChapter(1)
        }
    }

    fun clearAllDialog(): Boolean {
        return menuController.dismiss() ||
                dirController.dismiss()||
                fontSizeController.dismiss()||
                addBookmarkController.dismiss()||
                bookmarkController.dismiss()
    }

    fun setTextStyle(style: TextStyle) {
        _textStyle.value = style
        //可以加个变化检测
        saveReaderSetting()
        flushPages()
    }

    fun loadReaderSetting() {
        loadTextStyle()
    }

    fun loadTextStyle() {
        val fontSize = CustomedSettingService.readerFontSize.get()
        _textStyle.value = TextStyle(
            fontSize = fontSize.sp,
            fontFamily = FontFamily.Serif,
            lineHeight = (fontSize + 10).sp,
            letterSpacing = 0.5.sp
        )
    }

    fun saveReaderSetting() {
        CustomedSettingService.readerFontSize.set(textStyle.value.fontSize.value.toInt())
    }

    fun initialBookmark() {
        book?.let {
            bookmarkPopupCD.from(it, order.value, title.value, itemCallback = { item ->
                onClickBookmarkItem(item)
            }, addCallback = { note ->
                val dto = BookmarkRequestDto()
                dto.bookId = bookId
                dto.chapterId = chapterId
                dto.chapterTitle = title.value
                dto.order = order.value
                dto.note = note
                viewModelScope.launch {
                    val res = withContext(Dispatchers.IO) {
                        ConstValue.delay()
                        val bookmark = bookmarkPopupCD.getNowBookmark()
                        bookmark?.let {
                            bookmark.id?.let { it1 -> BookmarkService.updateBookmark(dto, it1) }
                        } ?: run{
                            BookmarkService.addBookmark(dto)
                        }
                    }
                    ConstValue.showOPstate(res != null)
                }
            })
        }
    }

    fun initialDir() {
        book?.let {
            dirPopupCD.from(it, order.value) { info ->
                onClickDirItem(info)
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
