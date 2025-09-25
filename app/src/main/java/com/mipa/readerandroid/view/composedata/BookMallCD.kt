package com.mipa.readerandroid.view.composedata

import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.model.feature.Book
import com.mipa.readerandroid.service.BookService
import androidx.navigation.NavHostController
import com.mipa.readerandroid.base.CDMap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BookMallCD : BooksShowViewModel() {


    override suspend fun getMoreData(pageNumber: Int, pageSize: Int): List<Book> {
        return BookService.getMoreBookList(pageNumber, pageSize)
    }


    override fun refresh() {
        if(datas.isEmpty())super.refresh()
    }

    override fun onItemClick(data: Book, naviController: NavHostController) {
        val bookDetailCD = CDMap.put(BookDetailCD())
        data.bookId?.let { bookDetailCD.from(data) }
        naviController.navigate(ConstValue.ROUTER_BOOK_DETAIL){
            launchSingleTop = true
        }
    }

}