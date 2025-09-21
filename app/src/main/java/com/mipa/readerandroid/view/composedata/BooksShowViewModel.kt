package com.mipa.readerandroid.view.composedata

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.model.feature.Book
import com.mipa.readerandroid.service.BookService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.navigation.NavHostController
import com.mipa.readerandroid.base.BaseCD

//todo 和DatasShowViewModel整合+使用协程而不是rxjava
abstract class BooksShowViewModel : BaseCD() {
    val books = mutableStateListOf<Book>()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _hasMoreData = MutableStateFlow(true)
    val hasMoreData: StateFlow<Boolean> = _hasMoreData

    private var currentPage = 0
    private val pageSize = 20


    fun loadMoreBooks() {
        if (_isLoading.value || !_hasMoreData.value) {
            return
        }
        _isLoading.value = true
        val dispatcher = Observable.fromCallable{
            ConstValue.delay()
            getMoreData(currentPage, pageSize)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ it ->
                val newBooks = it
                books.addAll(newBooks)
                currentPage++
                _hasMoreData.value = newBooks.size == pageSize
                _isLoading.value = false
            },
                { error ->
                    error.printStackTrace()
                    _isLoading.value = false
                }
            )
    }

    open fun refresh(){
        books.clear()
        _isLoading.value = false
        _hasMoreData.value = true
        currentPage = 0
        loadMoreBooks()
    }

    abstract fun getMoreData(pageNumber: Int, pageSize: Int):  List<Book>

    abstract fun onBookClick(book: Book, naviController: NavHostController)


}