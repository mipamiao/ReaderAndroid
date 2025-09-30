package com.mipa.readerandroid.base

object ThrowableOP {

    suspend fun <T> tryOP(default: T, content: suspend () -> T): T {
        try {
            return content()
        } catch (t: Throwable) {
            t.printStackTrace()
            return default
        }
    }

}