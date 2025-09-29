package com.mipa.readerandroid.view.reader

import android.util.Log
import androidx.navigation.NavHostController
import com.mipa.readerandroid.model.feature.Book
import com.mipa.readerandroid.model.feature.ChapterInfo
import com.mipa.readerandroid.service.ChapterService
import com.mipa.readerandroid.view.composedata.ChaptersShowViewModel
import com.mipa.readerandroid.view.composedata.DatasShowAllViewModel
import com.mipa.readerandroid.view.composedata.DatasShowViewModel

class DirPopupCD: DatasShowAllViewModel<ChapterInfo>() {
    companion object{
        const val TAG = "DirPopupCD"
    }

    var nowChapterIndex:Int = 0
    var onDirItemClick: (ChapterInfo)-> Unit = {}

    var book: Book? = null
    var bookId: String? = null


    override fun onItemClick(data: ChapterInfo, naviController: NavHostController) {
        onDirItemClick(data)
    }

    fun from(book: Book, nowChapterIndex: Int, callback: (ChapterInfo) -> Unit) {
        this.book = book
        bookId = book.bookId
        this.nowChapterIndex = nowChapterIndex
        this.onDirItemClick = callback
        Log.e(TAG, "from: ${this.book?.bookId ?: "null"}")
    }


    override suspend fun getAllData(): List<ChapterInfo> {
        Log.e(TAG, "getAllDatas: ${this.bookId ?: "null"}")
        return (bookId?.let { ChapterService.listAllChapters(it) }
            ?: emptyList()).sortedBy { info -> info.order }
    }
}