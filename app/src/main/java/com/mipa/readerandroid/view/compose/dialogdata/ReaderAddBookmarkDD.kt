package com.mipa.readerandroid.view.compose.dialogdata

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.mipa.readerandroid.base.BaseCD
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.base.dialogcontroller.DialogControllerWithAnim
import com.mipa.readerandroid.model.dto.BookmarkRequestDto
import com.mipa.readerandroid.model.feature.Bookmark
import com.mipa.readerandroid.model.feature.ChapterInfo
import com.mipa.readerandroid.service.BookmarkService
import com.mipa.readerandroid.view.reader.ReaderViewCD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReaderAddBookmarkDD: BaseCD() {
    companion object {
        const val TAG = "ReaderAddBookmarkDD"
    }


    val note = mutableStateOf("")

    var chapterInfo: ChapterInfo? = null
    var bookmark: Bookmark? = null
    var dialogController = DialogControllerWithAnim()

    fun init(controller: DialogControllerWithAnim){
        dialogController = controller
    }

    fun onSaveNoteClick(){
        chapterInfo?.let {chapterInfo->
            val dto = BookmarkRequestDto()
            dto.bookId = chapterInfo.bookId
            dto.chapterId = chapterInfo.chapterId
            dto.chapterTitle = chapterInfo.title
            dto.order = chapterInfo.order
            dto.note = note.value
            viewModelScope.launch {
                val res = withContext(Dispatchers.IO) {
                    ConstValue.delay()
                    bookmark?.let {bookmark->
                        bookmark.id?.let { it1 -> BookmarkService.updateBookmark(dto, it1) }
                    } ?: run{
                        BookmarkService.addBookmark(dto)
                    }
                }
                ConstValue.showOPstate(res != null)
            }
        }
        dialogController.dismiss()
    }

    fun open(chapterInfo: ChapterInfo, bookmark: Bookmark?){
        this.chapterInfo = chapterInfo
        this.bookmark = bookmark
        note.value = bookmark?.note?:""
        dialogController.show()
    }

    fun getBookmarkLoadState(): StateFlow<Boolean> {
        return CDMap.get<ReaderViewCD>().bookmarkDD.isLoading
    }
}