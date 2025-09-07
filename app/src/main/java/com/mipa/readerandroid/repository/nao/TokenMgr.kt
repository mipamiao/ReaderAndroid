package com.mipa.readerandroid.repository.nao

import com.mipa.readerandroid.base.SharePreferenceMgr

object TokenMgr {
    private var token: String = ""

    init {
        token = SharePreferenceMgr.getValue("jwtToken", "")
    }

    fun getToken(): String {
        return token
    }

    fun setToken(newValue: String) {
        if (token.equals(newValue)) return
        SharePreferenceMgr.setValue("jwtToken", newValue)
    }

}