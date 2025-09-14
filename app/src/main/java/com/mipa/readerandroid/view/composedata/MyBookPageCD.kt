package com.mipa.readerandroid.view.composedata

import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.mipa.readerandroid.R
import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.base.MyApp
import com.mipa.readerandroid.model.feature.Book
import com.mipa.readerandroid.service.BookService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

object MyBookPageCD: BooksShowViewModel(){

    val showEditDialog =  mutableStateOf(false)
    var currentBook: Book? = null

    override fun getMoreData(pageNumber: Int, pageSize: Int): List<Book> {
        return BookService.getMyBookList(pageNumber, pageSize)
    }

    override fun onBookClick(book: Book, naviController: NavHostController) {

    }

    fun onBookEditClick(book: Book, naviController: NavHostController){
        showEditDialog.value = true
        currentBook = book
    }

    fun onBookAddClick(){
        currentBook = Book()
        showEditDialog.value = true
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
                    val context = MyApp.getInstance().getContext()
                    val message =
                        if ( result) context.getString( R.string.book_info_op_success)
                        else context.getString( R.string.book_info_op_failed)
                    Toast.makeText(context, message , Toast.LENGTH_SHORT).show()
                    if(result)MyBookPageCD.books.remove(book)
                },
                { error ->
                    error.printStackTrace()
                }
            )
    }


}