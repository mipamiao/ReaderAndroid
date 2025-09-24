package com.mipa.readerandroid.base

import androidx.lifecycle.ViewModel

open class BaseCD : ViewModel() {



    open fun onBackClick(){
        CDMap.del(this)
    }
}