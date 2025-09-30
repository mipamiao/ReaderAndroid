package com.mipa.readerandroid.view.compose.dialogdata

import com.mipa.readerandroid.base.BaseCD
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.base.dialogcontroller.DialogController
import com.mipa.readerandroid.base.dialogcontroller.DialogControllerWithAnim
import com.mipa.readerandroid.view.reader.ReaderViewCD

class ReaderBotttomMenuDD: BaseCD() {

    var dialogController = DialogControllerWithAnim()

    fun init(controller: DialogControllerWithAnim){
        dialogController = controller
    }

    fun open(){
        dialogController.show()
    }

    fun onClickFontStyleItem() {

    }

    fun onClickFontSizeItem() {
        CDMap.get<ReaderViewCD>().apply {
            clearAllDialog()
            fontSizeController.show()
        }

    }

    fun onClickChapterListItem() {
        CDMap.get<ReaderViewCD>().apply {
            menuController.dismiss()
            chapterCache.chapter?.chapterInfo?.let { dirDD.open(it) }
        }
    }

    fun onClickOpenBookmarkItem() {
        CDMap.get<ReaderViewCD>().apply {
            menuController.dismiss()
            bookmarkDD.open()
        }
    }
}