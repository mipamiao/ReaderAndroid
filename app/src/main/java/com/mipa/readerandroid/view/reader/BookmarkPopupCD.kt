package com.mipa.readerandroid.view.reader

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.model.feature.Book
import com.mipa.readerandroid.model.feature.Bookmark
import com.mipa.readerandroid.service.BookmarkService
import com.mipa.readerandroid.view.composedata.DatasShowAllViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookmarkPopupCD : DatasShowAllViewModel<Bookmark>() {
    companion object {
        const val TAG = "BookmarkPopupCD"
    }

    var nowChapterIndex: Int = 0
    var nowChapterTitle: String = ""
    var onBookmarkItemClick: (Bookmark) -> Unit = {}
    var onSaveNoteClick: (String) -> Unit = {}

    var book: Book? = null
    var bookId: String? = null


    fun from(book: Book, nowChapterIndex: Int, nowChapterTitle: String, itemCallback: (Bookmark) -> Unit, addCallback: (String)-> Unit) {
        this.book = book
        bookId = book.bookId
        this.nowChapterIndex = nowChapterIndex
        this.nowChapterTitle = nowChapterTitle
        this.onBookmarkItemClick = itemCallback
        this.onSaveNoteClick = addCallback
        loadAlllDatas()
        Log.e(TAG, "from: ${this.book?.bookId ?: "null"}")
    }



    override suspend fun getAllData(): List<Bookmark> {
        return bookId?.let { BookmarkService.listAllBookmark(it) } ?: emptyList()
    }

    override fun onItemClick(data: Bookmark, naviController: NavHostController) {
        TODO("Not yet implemented")
    }

    fun getCurrentNote(): String {
        return datas.find { predict ->
            predict.order==nowChapterIndex
        }?.note?:""
    }

    fun isUpdate(): Boolean{
        var res =  datas.find { predict ->
            predict.order==nowChapterIndex }
        return res != null
    }

    fun getNowBookmark(): Bookmark?{
        return  datas.find { predict ->
            predict.order==nowChapterIndex }
    }

    fun onDelClick(bookmark: Bookmark) {
        viewModelScope.launch {
            val res = withContext(Dispatchers.IO) {
                ConstValue.delay()
                bookmark.id?.let { BookmarkService.delBookmark(it) }
            }
            if (res == true) {
                datas.remove(bookmark)
            }
            ConstValue.showOPstate(res)
        }
    }

}