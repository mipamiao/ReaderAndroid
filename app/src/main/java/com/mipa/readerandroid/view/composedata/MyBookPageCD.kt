package com.mipa.readerandroid.view.composedata

import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.mipa.readerandroid.R
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.base.MyApp
import com.mipa.readerandroid.model.feature.Book
import com.mipa.readerandroid.service.BookService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class MyBookPageCD: BooksShowViewModel(){

    val showEditDialog =  mutableStateOf(false)
    var currentBook: Book? = null

    override fun getMoreData(pageNumber: Int, pageSize: Int): List<Book> {
        return BookService.getMyBookList(pageNumber, pageSize)
    }

    override fun onBookClick(book: Book, naviController: NavHostController) {
        CDMap.get<MyChaptersPageCD>().from(book)
        naviController.navigate(ConstValue.ROUTER_MY_CHAPTERS_LIST)
    }

    fun onBookEditClick(book: Book, naviController: NavHostController){
        currentBook = Book()
        showEditDialog.value = true
        CDMap.get<BookInfoEditCD>().from(book)
    }

    fun onBookAddClick(){
        currentBook = Book()
        showEditDialog.value = true
        CDMap.get<BookInfoEditCD>().from(currentBook!!)
    }

    fun onBookRemoveClick(book: Book){
        if (book.bookId == null) return
        val disposable = Observable.fromCallable {
            ConstValue.delay()
            BookService.removeBook(book.bookId!!)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {result->
                    ConstValue.showOPstate(result)
                    if(result)books.remove(book)
                },
                { error ->
                    error.printStackTrace()
                }
            )
    }


}