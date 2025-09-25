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

    suspend fun getMoreBookList(pageNumber: Int, pageSize: Int, category: String? = null): List<Book> {
        try {
            val res = bookNao.getBookList(pageNumber, pageSize, category)
            if (res.isSuccess()) {
                res.data?.let {
                    return it.books
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "getMoreBookList: $e")
            return emptyList()
        }
        return emptyList()
    }

    suspend fun getMyBookList(pageNumber: Int, pageSize: Int): List<Book>{
        try {
            val res = bookNao.getMyBookList(pageNumber, pageSize, TokenMgr.getTokenWithPrefix())
            if(res.isSuccess()){
                res.data?.let {
                    return it.books
                }
            }
        }catch (e: Exception){
            Log.e(TAG, "getMyBookList: $e")
            return emptyList()
        }
        return emptyList()
    }

    suspend fun getBook(bookId: String): Book? {
        try {
            val res = bookNao.getBook(bookId)
            if (res.isSuccess()) {
                res.data?.let {
                    return it
                }
            }
        }catch (e: Exception){
            Log.e(TAG, "getBook: $e")
            return null
        }
        return null
    }

    suspend fun addBook(book: Book): Boolean{
        try {
            val res = bookNao.addBook(TokenMgr.getTokenWithPrefix(), book)
            return res.isSuccess()
        }catch (e: Exception){
            Log.e(TAG, "addBook: $e")
            return false
        }

    }

    suspend fun updateBook(bookId: String, book: Book): Boolean {
        try {
            val res = bookNao.updateBook(TokenMgr.getTokenWithPrefix(), book, bookId)
            return res.isSuccess()
        }catch (e: Exception){
            Log.e(TAG, "updateBook: $e")
            return false
        }
    }

    suspend fun removeBook(bookId: String): Boolean {
        try {
            val res = bookNao.removeBook(TokenMgr.getTokenWithPrefix(), bookId)
            return res.isSuccess()
        }catch (e: Exception){
            Log.e(TAG, "removeBook: $e")
            return false
        }
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