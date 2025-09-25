package com.mipa.readerandroid.view.composedata

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.model.dto.LibraryRequestDto
import com.mipa.readerandroid.model.feature.Book
import com.mipa.readerandroid.model.feature.ChapterInfo
import com.mipa.readerandroid.service.ChapterService
import com.mipa.readerandroid.service.LibraryService
import com.mipa.readerandroid.view.reader.ReaderViewCD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChapterListPageCD : ChaptersShowViewModel() {



    override suspend fun getMoreData(pageNumber: Int, pageSize: Int): List<ChapterInfo> {
        book.value.bookId?.let {
            val res = ChapterService.listChapters(it, pageNumber, pageSize)
            if (chapterNum == null) chapterNum = res.second
            return res.first
        }
        return emptyList()
    }

    fun addLibrary(chapterInfo: ChapterInfo){
        viewModelScope.launch {
            val res = withContext(Dispatchers.IO){
                val dto = LibraryRequestDto()
                dto.userId = CDMap.get<MePageCD>().userProfile.value.userId
                dto.bookId = book.value.bookId
                dto.lastReadChapterId = chapterInfo.chapterId
                LibraryService.updateLibrary(dto)
            }
            ConstValue.showOPstate(res)
        }
    }

    override fun from(book: Book){
        setBook(book)
    }

    override fun onItemClick(data: ChapterInfo, naviController: NavHostController) {
        CDMap.get<ReaderViewCD>().from(book.value.bookId, data.chapterId)
        addLibrary(data)
        naviController.navigate(ConstValue.ROUTER_READER_PAGE) {
            launchSingleTop = true
        }
    }

}