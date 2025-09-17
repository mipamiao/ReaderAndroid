package com.mipa.readerandroid.view.composedata

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.model.feature.Book
import com.mipa.readerandroid.model.feature.ChapterInfo
import com.mipa.readerandroid.service.ChapterService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class ChaptersShowViewModel : ViewModel() {

    val chapters = mutableStateListOf<ChapterInfo>()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _book = MutableStateFlow(Book())
    val book: StateFlow<Book> = _book

    abstract suspend fun getMoreData(): List<ChapterInfo>

    fun loadMoreData() {
        if (isLoading.value) return
        viewModelScope.launch {
            _isLoading.value = true
            withContext(Dispatchers.IO) {
                try {
                    ConstValue.delay()
                    book.value.bookId?.let {
                        chapters.addAll(getMoreData())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    _isLoading.value = false
                }
            }

        }
    }

    fun setBook(book: Book) {
        _book.value = book
        chapters.clear()
    }

    fun clearBooks(){
        chapters.clear()
    }

    fun onBackClick(naviController: NavHostController) {
        naviController.popBackStack()
    }
}