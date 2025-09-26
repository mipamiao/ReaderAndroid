package com.mipa.readerandroid.base.dialogcontroller

import androidx.compose.runtime.mutableStateOf

open class DialogController {

    val showState = mutableStateOf(false)
    val isShow: Boolean
        get() = showState.value
    var callBackSet: MutableSet<DialogCallBack> = mutableSetOf()

    open fun show(): Boolean {
        val result = !showState.value
        showState.value = true
        return result
    }

    fun onShow(){
        callBackSet.forEach {
            it.onShow()
        }
    }

    open fun dismiss(): Boolean {
        val result = showState.value
        showState.value = false
        return result
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