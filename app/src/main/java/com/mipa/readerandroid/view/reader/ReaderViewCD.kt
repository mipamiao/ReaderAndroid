package com.mipa.readerandroid.view.reader

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavHostController
import com.mipa.readerandroid.base.BaseCD
import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.base.EffectController.EffectController
import com.mipa.readerandroid.base.dialogcontroller.DialogControllerWithAnim
import com.mipa.readerandroid.view.composedata.base.ChapterCache
import com.mipa.readerandroid.view.composedata.base.ChaptersCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.max

class ReaderViewCD: BaseCD() {

    var bookId: String? = null
    var chapterId: String? = null
    var orderNum: Int? = null


    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    val menuController = DialogControllerWithAnim()


    val chaptersCache = ChaptersCache()
    var chapterCache = ChapterCache()

    private val _order  = mutableStateOf(0)
    val order: State<Int> = _order

    private val _content = mutableStateOf("")
    val content: State<String> = _content

    private val _title = mutableStateOf("")
    val title: State<String> = _title

    val loadChapterTrigger = EffectController()


    fun from(bookId: String?, chapterId: String?){
        this.bookId = bookId
        this.chapterId = chapterId
    }

    fun from(bookId: String, order: Int, orderNum: Int) {
        this.bookId = bookId
        this._order.value = order
        this.orderNum = orderNum
        chaptersCache.orderNum = orderNum
        chaptersCache.bookId = bookId
    }


    @OptIn(ExperimentalFoundationApi::class)
    fun lastPage(pagerState: PagerState, coroutineScope: CoroutineScope) {
        if (menuController.dismiss()) return
        //var target  =
         {
            coroutineScope.launch {
                pagerState.animateScrollToPage(max(pagerState.currentPage - 1, 0))
            }
        }

    }

    @OptIn(ExperimentalFoundationApi::class)
    fun nextPage(pagerState: PagerState, coroutineScope: CoroutineScope) {
        if (menuController.dismiss()) return
        val target = if (pagerState.settledPage == pagerState.pageCount - 1) {
            nextChapter()
            0
        } else {
            pagerState.settledPage + 1
        }
        coroutineScope.launch {
            pagerState.scrollToPage(target)
        }
    }

    fun lastChapter() {
        if (order.value == 0) {
            ConstValue.showToast("没有上一章啦")
            return
        }
        _order.value--
    }

    fun nextChapter() {
        if (order.value == orderNum!! - 1) {
            ConstValue.showToast("没有下一章啦")
            return
        }
        _order.value ++
    }

    fun loadChapter() {
        chapterCache = chaptersCache.get(order.value, onEnd = { chapter ->
            chapter?.let { chapter
                chapter.content?.let {
                    _content.value = it
                }
                chapter.chapterInfo?.title?.let {
                    _title.value = it
                }
            }
        })
    }


    fun switchMenu(){
        menuController.switch()
    }


    fun onClickFontStyleItem() {

    }

    fun onClickFontSizeItem() {

    }

    fun onClickChapterListItem() {

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
}