package com.mipa.readerandroid.view.compose.dialogdata

import androidx.navigation.NavHostController
import com.mipa.readerandroid.base.BaseCD
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.base.dialogcontroller.DialogControllerWithAnim
import com.mipa.readerandroid.view.reader.ReaderViewCD

class ReaderTopMenuDD: BaseCD() {

    var dialogController = DialogControllerWithAnim()

    fun init(controller: DialogControllerWithAnim){
        dialogController = controller
    }

    fun open(){
        dialogController.show()
    }

    fun onClickBack(naviController: NavHostController){
        naviController.popBackStack()
    }

    fun onClickListenBook(){

    }

    fun onClickComment(){

    }

    fun onClickAddBookmark() {
        CDMap.get<ReaderViewCD>().apply {
            clearAllDialog()
            chapterCache.chapter?.chapterInfo?.let { addBookmarkDD.open(it, bookmarkDD.getCurrentBookmark(order.value)) }
        }
    }
}