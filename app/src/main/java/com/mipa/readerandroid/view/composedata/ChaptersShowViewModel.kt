package com.mipa.readerandroid.view.composedata

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.mipa.readerandroid.base.BaseCD
import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.model.feature.Book
import com.mipa.readerandroid.model.feature.ChapterInfo
import com.mipa.readerandroid.service.ChapterService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class ChaptersShowViewModel : DatasShowViewModel<ChapterInfo>() {


    private val _book = MutableStateFlow(Book())
    val book: StateFlow<Book> = _book

    var chapterNum: Int? = null

    fun setBook(book: Book) {
        _book.value = book
        datas.clear()
    }

    fun clearBooks(){
        datas.clear()
    }

    fun onBackClick(naviController: NavHostController) {
        naviController.popBackStack()
    }

    open fun from(book: Book){
        setBook(book)
    }
}