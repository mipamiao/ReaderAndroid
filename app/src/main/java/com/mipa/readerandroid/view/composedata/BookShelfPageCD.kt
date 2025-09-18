package com.mipa.readerandroid.view.composedata

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.model.feature.Library
import com.mipa.readerandroid.service.LibraryService
import com.mipa.readerandroid.view.reader.ReaderViewCD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object BookShelfPageCD : DatasShowViewModel<Library>() {

    override suspend fun getMoreData(pageNumber: Int, pageSize: Int): List<Library> {
        return LibraryService.listLibrary(pageNumber, pageSize)
    }

    override fun onBookClick(data: Library, naviController: NavHostController) {
        ReaderViewCD.bookId = data.book?.bookId
        ReaderViewCD.chapterId = data.chapterInfo?.chapterId
        naviController.navigate(ConstValue.ROUTER_READER_PAGE)
    }

    fun onDeleteClick(data: Library) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                ConstValue.delay(1000)
                data.book?.bookId?.let { LibraryService.removeLibrary(it) }
            }
            if (result == true)
                datas.remove(data)
            ConstValue.showOPstate(result)
        }
    }

}