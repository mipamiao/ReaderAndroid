package com.mipa.readerandroid.view.composedata

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.mipa.readerandroid.R
import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.base.MyApp
import com.mipa.readerandroid.model.feature.Book
import com.mipa.readerandroid.service.BookService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class BookInfoEditCD(val book: Book): ViewModel() {
    val coverImage = mutableStateOf("")
    val title = mutableStateOf("")
    val description = mutableStateOf("")
    val category = mutableStateOf("")
    val tags = mutableStateListOf<String>()

    var disposable: Disposable? = null

    private val _isSaving = mutableStateOf(false)
    val isSaving:State<Boolean> = _isSaving

    init {
        coverImage.value = book.coverImage ?: ""
        title.value = book.title ?: ""
        description.value = book.description ?: ""
        category.value = book.category ?: ""
        book.tags?.let { tags.addAll(it) }
        updateCount()
    }

    fun onSave(quit: () -> Unit = {}){
        if(_isSaving.value)return

        _isSaving.value = true
        reWrite()
        disposable = Observable.fromCallable{
            ConstValue.delay()
            book.bookId?.let {
                BookService.updateBook(it, book)
            } ?: (BookService.addBook(book))
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {result->
                    val context = MyApp.getInstance().getContext()
                    val message =
                        if ( result) context.getString( R.string.book_info_op_success)
                        else context.getString( R.string.book_info_op_failed)
                    Toast.makeText(context, message , Toast.LENGTH_SHORT).show()
                    MyBookPageCD.refresh()
                    quit()
                    _isSaving.value = false
                },
                { error ->
                    error.printStackTrace()
                    _isSaving.value = false
                }
            )
    }

    private fun reWrite(){
        book.coverImage = coverImage.value
        book.title = title.value
        book.description = description.value
        book.category = category.value
        book.tags = tags
    }

    companion object{
        var count = 0
        fun updateCount(){
            count ++
            Log.e("TAG", "updateCount: $count", )
        }
    }
}
