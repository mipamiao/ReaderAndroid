package com.mipa.readerandroid.view.composedata

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.mipa.readerandroid.base.BaseCD
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.model.feature.Book
import com.mipa.readerandroid.service.BookService
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

class BookDetailCD :BaseCD() {
    private val _bookId = mutableStateOf("")
    val bookId: State<String> = _bookId

    private val _book = mutableStateOf(Book())
    val book: State<Book> = _book

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    var disposable:Disposable? = null

    fun updateBook(boolId: String){
        _bookId.value = boolId
    }

    fun from(book: Book){
        _book.value = book
    }

    fun loadBook() {
        if (_isLoading.value) return
        _isLoading.value = true
        viewModelScope.launch {
            val res = withContext(Dispatchers.IO) {
                ConstValue.delay()
                BookService.getBook(_bookId.value)
            }
            res?.let { _book.value = res }
            _isLoading.value = false
        }
    }

    fun cancelLoad(){
        disposable?.dispose()
        disposable = null
    }

    fun onClickDirItem(naviController: NavHostController) {
        CDMap.put(ChapterListPageCD()).from(book.value)
        naviController.navigate(ConstValue.ROUTER_CHAPTER_LIST){
            launchSingleTop = true
        }
    }


}