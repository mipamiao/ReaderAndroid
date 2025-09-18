package com.mipa.readerandroid.view.composedata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.model.dto.LibraryRequestDto
import com.mipa.readerandroid.model.feature.Book
import com.mipa.readerandroid.model.feature.Chapter
import com.mipa.readerandroid.model.feature.ChapterInfo
import com.mipa.readerandroid.service.ChapterService
import com.mipa.readerandroid.service.LibraryService
import com.mipa.readerandroid.view.reader.ReaderViewCD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

object ChapterListPageCD : ChaptersShowViewModel() {

    override suspend fun getMoreData(): List<ChapterInfo> {
        book.value.bookId?.let {
            return ChapterService.listChapters(it)
        }
        return emptyList()
    }

    fun onChapterClick(chapterInfo: ChapterInfo, naviControllrt: NavHostController){
        ReaderViewCD.bookId = book.value.bookId
        ReaderViewCD.chapterId = chapterInfo.chapterId
        addLibrary(chapterInfo)
        naviControllrt.navigate(ConstValue.ROUTER_READER_PAGE) {
            launchSingleTop = true
        }
    }

    fun addLibrary(chapterInfo: ChapterInfo){
        viewModelScope.launch {
            val res = withContext(Dispatchers.IO){
                val dto = LibraryRequestDto()
                dto.userId = MePageCD.userProfile.value.userId
                dto.bookId = book.value.bookId
                dto.lastReadChapterId = chapterInfo.chapterId
                LibraryService.updateLibrary(dto)
            }
            ConstValue.showOPstate(res)
        }

    }

}