package com.mipa.readerandroid.view.compose.dialogdata

import android.util.Log
import androidx.navigation.NavHostController
import com.mipa.readerandroid.base.BaseCD
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.base.dialogcontroller.DialogControllerWithAnim
import com.mipa.readerandroid.model.feature.Book
import com.mipa.readerandroid.model.feature.ChapterInfo
import com.mipa.readerandroid.service.ChapterService
import com.mipa.readerandroid.view.composedata.DatasShowAllViewModel
import com.mipa.readerandroid.view.reader.ReaderViewCD

class ReaderDirDD: DatasShowAllViewModel<ChapterInfo>() {
    companion object{
        const val TAG = "ReaderDirDD"
    }

    var chapterInfo: ChapterInfo? = null
    var book: Book? = null

    var dialogController = DialogControllerWithAnim()

    fun init(book: Book, controller: DialogControllerWithAnim ) {
        this.book = book
        dialogController = controller
    }

    override fun onItemClick(data: ChapterInfo, naviController: NavHostController) {
        val readerViewCD = CDMap.get<ReaderViewCD>()
        readerViewCD.clearAllDialog()
        data.order?.let {
            readerViewCD.setOrder(it)
            readerViewCD.loadChapter(1)
        }
    }

    override suspend fun getAllData(): List<ChapterInfo> {
        return (book?.bookId?.let { ChapterService.listAllChapters(it) }
            ?: emptyList()).sortedBy { info -> info.order }
    }

    fun open(chapterInfo: ChapterInfo) {
        this.chapterInfo = chapterInfo
        dialogController.show()
    }

}