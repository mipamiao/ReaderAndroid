package com.mipa.readerandroid.view.compose.dialogdata

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.mipa.readerandroid.base.BaseCD
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.base.dialogcontroller.DialogControllerWithAnim
import com.mipa.readerandroid.model.feature.Book
import com.mipa.readerandroid.model.feature.Bookmark
import com.mipa.readerandroid.service.BookmarkService
import com.mipa.readerandroid.view.composedata.DatasShowAllViewModel
import com.mipa.readerandroid.view.reader.ReaderViewCD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReaderBookmarkDD : DatasShowAllViewModel<Bookmark>() {
    companion object {
        const val TAG = "ReaderBookmarkDD"
    }

    var book: Book? = null
    var dialogController = DialogControllerWithAnim()

    fun init(book: Book, controller: DialogControllerWithAnim) {
        this.book = book
        dialogController = controller
        loadAlllDatas()
    }

    override suspend fun getAllData(): List<Bookmark> {
        return book?.bookId?.let { BookmarkService.listAllBookmark(it) } ?: emptyList()
    }

    override fun onItemClick(data: Bookmark, naviController: NavHostController) {
        val readerViewCD = CDMap.get<ReaderViewCD>()
        readerViewCD.clearAllDialog()
        data.order?.let {
            readerViewCD.setOrder(it)
            readerViewCD.loadChapter(1)
        }
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

    fun getCurrentBookmark(order: Int): Bookmark?{
        return  datas.find { item->
            item.order == order
        }
    }

    fun open(){
        dialogController.show()
    }
}