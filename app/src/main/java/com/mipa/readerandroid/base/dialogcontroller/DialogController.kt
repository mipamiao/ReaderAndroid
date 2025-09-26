package com.mipa.readerandroid.base.dialogcontroller

import androidx.compose.runtime.mutableStateOf

open class DialogController {

    val showState = mutableStateOf(false)
    val isShow: Boolean
        get() = showState.value
    var callBackSet: MutableSet<DialogCallBack> = mutableSetOf()

    open fun show() {
        showState.value = true
    }

    fun onShow(){
        callBackSet.forEach {
            it.onShow()
        }
    }

    open fun dismiss() {
        showState.value = false
    }

    fun onDismiss(){
        callBackSet.forEach {
            it.onDismiss()
        }
    }

    fun addCallBack(callBack: DialogCallBack) {
        callBackSet.add(callBack)
    }

    fun removeCallBack(callBack: DialogCallBack) {
        callBackSet.remove(callBack)
    }
}

interface DialogCallBack {
    fun onDismiss() {}
    fun onShow() {}
}