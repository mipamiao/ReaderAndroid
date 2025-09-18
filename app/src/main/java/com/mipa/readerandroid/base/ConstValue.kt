package com.mipa.readerandroid.base

import android.widget.Toast
import com.mipa.readerandroid.R
import com.mipa.readerandroid.model.feature.Book
import com.mipa.readerandroid.view.composedata.MyBookPageCD

class ConstValue {
    companion object{

        const val ROUTER_BOOKMALL = "bookmall"
        const val ROUTER_MEPAGE = "mepage"
        const val ROUTER_REGISTER = "registerpage"
        const val ROUTER_LOGIN = "loginpage"
        const val ROUTER_ME_DETAIL = "mepagedetail"
        const val ROUTER_BOOK_DETAIL = "bookdetail"
        const val ROUTER_MY_BOOKS = "mybooks"
        const val ROUTER_CHAPTER_LIST = "chapterlist"
        const val ROUTER_MY_CHAPTERS_LIST = "mychapterslist"
        const val ROUTER_READER_PAGE = "raderpage"
        const val ROUTER_WRITER_PAGE = "writerpage"
        const val ROUTER_BOOK_SHELF = "bookshelf"
        const val ROUTER_SEARCH_PAGE = "searchpage"


        fun delay(mill: Long = 2000) {
            Thread.sleep(mill)
        }

        fun showOPstate(result: Boolean?) {
            val context = MyApp.getInstance().getContext()
            val message =
                if (result == true) context.getString(R.string.book_info_op_success)
                else context.getString(R.string.book_info_op_failed)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}