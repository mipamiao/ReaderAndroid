package com.mipa.readerandroid.base

class ConstValue {
    companion object{
        const val ROUTER_BOOKMALL = "bookmall"
        const val ROUTER_MEPAGE = "mepage"
        const val ROUTER_REGISTER = "registerpage"
        const val ROUTER_LOGIN = "loginpage"
        const val ROUTER_ME_DETAIL = "mepagedetail"
        const val ROUTER_BOOK_DETAIL = "bookdetail"
        const val ROUTER_MY_BOOKS = "mybooks"


        public fun delay(){
            Thread.sleep(2000)
        }
    }
}