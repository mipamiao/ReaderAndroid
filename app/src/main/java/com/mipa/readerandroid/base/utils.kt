package com.mipa.readerandroid.base

import com.mipa.readerandroid.repository.AppNet


fun String.combineHost(): String {
    return AppNet.BASE_URL +"/" + this.trimStart('/')
}