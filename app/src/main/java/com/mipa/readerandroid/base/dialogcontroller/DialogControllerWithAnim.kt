package com.mipa.readerandroid.base.dialogcontroller

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

class DialogControllerWithAnim: DialogController() {

    private val _state = mutableStateOf(DialogState.dismiss)
    val state: State<DialogState> = _state

    fun canShow(): Boolean{
        return state.value != DialogState.dismiss
    }

    override fun show() {
        if (_state.value == DialogState.dismiss) {
            showState.value = true
            _state.value = DialogState.showing
        }
    }

    override fun dismiss() {
        if (_state.value == DialogState.show) {
            showState.value = false
            _state.value = DialogState.dismissing
        }
    }

    fun switch(){
        when(state.value){
            DialogState.showing, DialogState.dismissing -> return
            DialogState.show->dismiss()
            DialogState.dismiss->show()
        }
    }

    fun endShowing() {
        _state.value = DialogState.show
    }

    fun endDismissing() {
        _state.value = DialogState.dismiss
    }

}
enum class DialogState {
    show, showing, dismiss, dismissing
}