package com.mipa.readerandroid.view.composedata

import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.model.feature.Book
import com.mipa.readerandroid.service.BookService
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.single

object BookMallCD : BooksShowViewModel() {

    private val _needFlush = MutableStateFlow(false)
    val needFlush: StateFlow<Boolean> = _needFlush

    override fun getMoreData(pageNumber: Int, pageSize: Int): List<Book> {
        return BookService.getMoreBookList(pageNumber, pageSize)
    }


    override fun onBookClick(book: Book, naviController: NavHostController) {
        book.bookId?.let { BookDetailCD.updateBook(it) }
        naviController.navigate(ConstValue.ROUTER_BOOK_DETAIL){
            launchSingleTop = true
        }
    }

    override fun refresh() {
        if(books.isEmpty())super.refresh()
    }

}