package com.mipa.readerandroid.view.composedata

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.mipa.readerandroid.base.dialogcontroller.BookInfoEditDialogController
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.model.feature.Book
import com.mipa.readerandroid.service.BookService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyBookPageCD: BooksShowViewModel(){


    val bookInfoEditDialogController = BookInfoEditDialogController()

    override suspend fun getMoreData(pageNumber: Int, pageSize: Int): List<Book> {
        return BookService.getMyBookList(pageNumber, pageSize)
    }

    override fun onItemClick(data: Book, naviController: NavHostController) {
        CDMap.get<MyChaptersPageCD>().from(data)
        naviController.navigate(ConstValue.ROUTER_MY_CHAPTERS_LIST)
    }

    fun onBookEditClick(book: Book, naviController: NavHostController){
        bookInfoEditDialogController.book = Book()
        bookInfoEditDialogController.show()
        CDMap.get<BookInfoEditCD>().from(book)
    }

    fun onBookAddClick(){
        bookInfoEditDialogController.book = Book()
        bookInfoEditDialogController.show()
        CDMap.get<BookInfoEditCD>().from(bookInfoEditDialogController.book!!)
    }

    fun onBookRemoveClick(book: Book) {
        book.bookId?.let { bookId ->
            viewModelScope.launch {
                val res = withContext(Dispatchers.IO) {
                    ConstValue.delay()
                    BookService.removeBook(bookId)
                }
                ConstValue.showOPstate(res)
                if (res) datas.remove(book)
            }
        }
    }


}