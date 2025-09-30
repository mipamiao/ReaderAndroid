package com.mipa.readerandroid.view.compose.dialogdata

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.mipa.readerandroid.base.BaseCD
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.base.dialogcontroller.DialogControllerWithAnim
import com.mipa.readerandroid.view.reader.ReaderViewCD

class ReaderFontSizeDD: BaseCD() {

    val fontSize = mutableStateOf(18)

    var dialogController = DialogControllerWithAnim()

    fun init(controller: DialogControllerWithAnim) {
        dialogController = controller
    }

    fun open(fontSize: Int){
        CDMap.get<ReaderViewCD>().apply {
            clearAllDialog()
        }
        this.fontSize.value = fontSize
        dialogController.show()
    }

    fun onFontSizeChanged(){
        val value = fontSize.value
        CDMap.get<ReaderViewCD>().apply {
            val style = textStyle.value
            setTextStyle(
                TextStyle(
                    lineHeight = (value + 10).sp,
                    fontSize = value.sp,
                    fontFamily = style.fontFamily,
                    letterSpacing = style.letterSpacing
                )
            )
        }
    }
}