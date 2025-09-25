package com.mipa.readerandroid.view.composedata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.model.feature.ChapterInfo
import com.mipa.readerandroid.service.ChapterService
import com.mipa.readerandroid.view.compose.writer.WriterViewCD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyChaptersPageCD: ChaptersShowViewModel(){

    override suspend fun getMoreData(pageNumber: Int, pageSize: Int): List<ChapterInfo> {
        book.value.bookId?.let {
            val res = ChapterService.listChapters(it, pageNumber, pageSize)
            if (chapterNum == null) chapterNum = res.second
            return res.first
        }
        return emptyList()
    }

    override fun onItemClick(data: ChapterInfo, naviController: NavHostController) {
        onEditClick(data, naviController)
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
            if (result == true) datas.remove(chapterInfo)
        }
    }

    fun onEditClick(chapterInfo: ChapterInfo, naviController: NavHostController) {
        CDMap.get<WriterViewCD>().from(chapterInfo.bookId, chapterInfo.chapterId)
        naviController.navigate(ConstValue.ROUTER_WRITER_PAGE) {
            launchSingleTop = true
        }
    }

    fun onAddClick(naviController: NavHostController) {
        CDMap.get<WriterViewCD>().from(book.value.bookId, null,datas.size + 1)
        naviController.navigate(ConstValue.ROUTER_WRITER_PAGE) {
            launchSingleTop = true
        }
    }

}