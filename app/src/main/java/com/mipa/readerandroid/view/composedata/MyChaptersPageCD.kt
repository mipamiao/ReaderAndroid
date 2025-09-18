package com.mipa.readerandroid.view.composedata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.model.feature.ChapterInfo
import com.mipa.readerandroid.service.ChapterService
import com.mipa.readerandroid.view.compose.writer.WriterViewCD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object MyChaptersPageCD: ChaptersShowViewModel(){

    override suspend fun getMoreData(): List<ChapterInfo> {
        book.value.bookId?.let {
            return ChapterService.listChapters(it)
        }
        return emptyList()
    }

    fun onDeleteClick(chapterInfo: ChapterInfo) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                ConstValue.delay()
                book.value.bookId?.let { bookId ->
                    chapterInfo.chapterId?.let { chapterId ->
                        ChapterService.removeChapter(bookId, chapterId)
                    }
                }
            }
            ConstValue.showOPstate(result)
            if (result == true) chapters.remove(chapterInfo)
        }
    }

    fun onEditClick(chapterInfo: ChapterInfo, naviController: NavHostController) {
        WriterViewCD.bookId = book.value.bookId
        WriterViewCD.chapterId = chapterInfo.chapterId
        naviController.navigate(ConstValue.ROUTER_WRITER_PAGE) {
            launchSingleTop = true
        }
    }

    fun onAddClick(naviController: NavHostController) {
        WriterViewCD.bookId = book.value.bookId
        WriterViewCD.order = chapters.size + 1
        naviController.navigate(ConstValue.ROUTER_WRITER_PAGE) {
            launchSingleTop = true
        }
    }

}