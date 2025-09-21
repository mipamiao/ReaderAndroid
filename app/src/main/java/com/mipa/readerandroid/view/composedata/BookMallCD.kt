package com.mipa.readerandroid.view.composedata

import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.model.feature.Book
import com.mipa.readerandroid.service.BookService
import androidx.navigation.NavHostController
import com.mipa.readerandroid.base.CDMap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BookMallCD : BooksShowViewModel() {

    private val _needFlush = MutableStateFlow(false)
    val needFlush: StateFlow<Boolean> = _needFlush

    override fun getMoreData(pageNumber: Int, pageSize: Int): List<Book> {
        return BookService.getMoreBookList(pageNumber, pageSize)
    }


    override fun onBookClick(book: Book, naviController: NavHostController) {
        val bookDetailCD = CDMap.put(BookDetailCD())
        book.bookId?.let { bookDetailCD.from(book) }
        naviController.navigate(ConstValue.ROUTER_BOOK_DETAIL){
            launchSingleTop = true
        }
    }

    override fun refresh() {
        if(books.isEmpty())super.refresh()
    }

}