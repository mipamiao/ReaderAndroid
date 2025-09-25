package com.mipa.readerandroid.view.composedata

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.mipa.readerandroid.base.BaseCD
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.base.MyApp
import com.mipa.readerandroid.model.feature.Book
import com.mipa.readerandroid.service.BookService
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookInfoEditCD(): BaseCD() {

    private var book: Book? = null

    val coverImage = mutableStateOf("")
    val title = mutableStateOf("")
    val description = mutableStateOf("")
    val category = mutableStateOf("")
    val tags = mutableStateListOf<String>()

    var disposable: Disposable? = null

    private val _isSaving = mutableStateOf(false)
    val isSaving:State<Boolean> = _isSaving


    fun onSave(quit: () -> Unit = {}) {
        if (_isSaving.value) return

        _isSaving.value = true
        reWrite()

        viewModelScope.launch {
            val res = withContext(Dispatchers.IO) {
                ConstValue.delay()
                book?.let { book ->
                    book.bookId?.let {
                        BookService.updateBook(it, book)
                    } ?: (BookService.addBook(book))
                } ?: false
            }
            val context = MyApp.getInstance().getContext()
            ConstValue.showOPstate(res)
            CDMap.get<MyBookPageCD>().refresh()
            quit()
            _isSaving.value = false
        }
    }


    fun updateCoverImg(uri: Uri, quit: () -> Unit = {}) {
        if(_isSaving.value)return
        _isSaving.value = true
        viewModelScope.launch {
            val url = withContext(Dispatchers.IO) {
                ConstValue.delay()
                book?.bookId?.let {
                    BookService.updateCoverImg(uri, it)
                }
            }
            ConstValue.showOPstate(url != null)
            url?.let {
                coverImage.value = it
                CDMap.get<MyBookPageCD>().refresh()
                quit()
            }
            _isSaving.value = false
        }
    }

    private fun reWrite(){
        book?.let {book->
            book.coverImage = coverImage.value
            book.title = title.value
            book.description = description.value
            book.category = category.value
            book.tags = tags
        }
    }

    fun from(book: Book){
        coverImage.value = book.coverImage?:""
        title.value = book.title?:""
        description.value = book.description?:""
        category.value = book.category?:""
        tags.clear()
        tags.addAll(book.tags?: emptyList())
        this.book = book
    }


    companion object{
        var count = 0
        fun updateCount(){
            count ++
            Log.e("TAG", "updateCount: $count", )
        }
    }
}
