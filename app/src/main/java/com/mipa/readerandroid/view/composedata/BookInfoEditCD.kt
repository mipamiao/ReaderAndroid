package com.mipa.readerandroid.view.composedata

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mipa.readerandroid.R
import com.mipa.readerandroid.base.BaseCD
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.base.MyApp
import com.mipa.readerandroid.model.feature.Book
import com.mipa.readerandroid.repository.AppNet
import com.mipa.readerandroid.service.BookService
import com.mipa.readerandroid.service.UserService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
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


    fun onSave(quit: () -> Unit = {}){
        if(_isSaving.value)return

        _isSaving.value = true
        reWrite()
        disposable = Observable.fromCallable{
            ConstValue.delay()
            book?.let { book ->
                book.bookId?.let {
                    BookService.updateBook(it, book)
                } ?: (BookService.addBook(book))
            }?:false
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {result->
                    val context = MyApp.getInstance().getContext()
                    ConstValue.showOPstate(result)// todo 由服务端决定展示什么toast消息，服务端未下发再由客户端兜底
                    CDMap.get<MyBookPageCD>().refresh()
                    quit()
                    _isSaving.value = false
                },
                { error ->
                    error.printStackTrace()
                    _isSaving.value = false
                }
            )
    }

    //todo 将更新封面和其它数据的入口分开就可以解决
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
