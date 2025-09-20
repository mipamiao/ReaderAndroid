package com.mipa.readerandroid.service

import android.net.Uri
import android.util.Log
import com.mipa.readerandroid.base.MyApp
import com.mipa.readerandroid.model.feature.Book
import com.mipa.readerandroid.repository.AppNet
import com.mipa.readerandroid.repository.nao.ApiResponse
import com.mipa.readerandroid.repository.nao.TokenMgr
import com.mipa.readerandroid.repository.util.FileUtils
import com.mipa.readerandroid.service.UserService.TAG
import com.mipa.readerandroid.service.UserService.userNao

//todo 还是数据类的问题，到底该怎么合理的使用数据类和实体类，就算这两个相同也要独立定义吗
object BookService {

    val bookNao by lazy {
        AppNet.bookNao()
    }

    fun getMoreBookList(pageNumber: Int, pageSize: Int, category: String? = null): List<Book> {
        val call = bookNao.getBookList(pageNumber, pageSize, category)
        val res = call.execute()
        var books: List<Book> = listOf()
        if (res.isSuccessful) {
            res.body()?.let {
                if (it.isSuccess() && it.data != null) {
                    books = it.data.books
                }
            }
        }
        return books
    }

    fun getMyBookList(pageNumber: Int, pageSize: Int): List<Book>{
        val call = bookNao.getMyBookList(pageNumber, pageSize, TokenMgr.getTokenWithPrefix())
        val res = call.execute()
        var books: List<Book> = listOf()
        if (res.isSuccessful) {
            res.body()?.let {
                if (it.isSuccess() && it.data != null) {
                    books = it.data.books?:listOf()
                }
            }
        }
        return books
    }

    fun getBook(bookId: String): Book? {
        val call = bookNao.getBook(bookId)
        val res = call.execute()
        if (res.isSuccessful) {
            res.body()?.let {
                if (it.isSuccess() && it.data != null) {
                    return it.data
                }
            }
        }
        return null
    }

    fun addBook(book: Book): Boolean{
        val call = bookNao.addBook(TokenMgr.getTokenWithPrefix(), book)
        val res = call.execute()
        if (res.isSuccessful) {
            res.body()?.let {
                if (it.isSuccess()) {
                    return true
                }
            }
        }
        return false
    }

    fun updateBook(bookId: String, book: Book): Boolean {
        val call = bookNao.updateBook(TokenMgr.getTokenWithPrefix(), book, bookId)
        val res = call.execute()
        if (res.isSuccessful) {
            res.body()?.let {
                if (it.isSuccess()) {
                    return true
                }
            }
        }
        return false
    }

    fun removeBook(bookId: String): Boolean {
        val call = bookNao.removeBook(TokenMgr.getTokenWithPrefix(), bookId)
        val res = call.execute()
        if (res.isSuccessful) {
            res.body()?.let {
                if (it.isSuccess()) {
                    return true
                }
            }
        }
        return false
    }

    suspend fun updateCoverImg(uri: Uri, bookId: String): String? {
        val context = MyApp.getInstance().getContext()
        return try {
            val requestFile = FileUtils.createPart(context, uri, "coverImg")
            requestFile?.let {
                val response: ApiResponse<String> =
                    bookNao.updateCoverImg(TokenMgr.getTokenWithPrefix(), it, bookId)
                if (response.isSuccess()) {
                    response.data
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "updateCoverImg: $e")
            null
        }
    }



}