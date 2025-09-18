package com.mipa.readerandroid.view.composedata

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.model.feature.Book
import com.mipa.readerandroid.service.BookService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Optional

object BookDetailCD :ViewModel() {
    private val _bookId = mutableStateOf("")
    val bookId: State<String> = _bookId

    private val _book = mutableStateOf(Book())
    val book: State<Book> = _book

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    var disposable:Disposable? = null

    fun updateBook(boolId: String){
        _bookId.value = boolId
    }

    fun loadBook(){
        disposable = Observable.fromCallable {
            _isLoading.value = true
            ConstValue.delay()
            Optional.ofNullable(BookService.getBook(_bookId.value))
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ bookOpt ->
                bookOpt.ifPresent { _book.value = bookOpt.get() }
                _isLoading.value = false
            }, { error ->
                error.printStackTrace()
                _isLoading.value = false
            })
    }

    fun cancelLoad(){
        disposable?.dispose()
        disposable = null
    }


}