package com.mipa.readerandroid.view.reader

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.mipa.readerandroid.base.BaseCD
import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.base.dialogcontroller.DialogControllerWithAnim
import com.mipa.readerandroid.model.feature.Chapter
import com.mipa.readerandroid.service.ChapterService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReaderViewCD: BaseCD() {

    var bookId: String? = null
    var chapterId: String? = null

    private val _chapter = MutableStateFlow(Chapter(null, null))
    val chapter: StateFlow<Chapter> = _chapter

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    val menuController = DialogControllerWithAnim()

    val  pages = mutableStateListOf<String>()

    fun getData() {
        if (isLoading.value) return
        _isLoading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                ConstValue.delay()
                bookId?.let { bookId ->
                    chapterId?.let { chapterId ->
                        val res = ChapterService.getChapter(bookId, chapterId)
                        res?.let { _chapter.value = it }
                    }
                }
            }
            _isLoading.value = false
        }
    }

    fun from(bookId: String?, chapterId: String?){
        this.bookId = bookId
        this.chapterId = chapterId
    }

    fun addAllToPages(extraPages: List<String>){
        pages.addAll(extraPages)
    }

    fun lastPage(){
        menuController.dismiss()
    }

    fun nextPage(){
        menuController.dismiss()
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