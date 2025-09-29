package com.mipa.readerandroid.base

class PersistValue<T>(val defaultValue: T, val key: String) {
    private var value: T? = null

    fun get(): T{
        return value ?: SharePreferenceMgr.getValue(key, defaultValue).also { value = it }
    }

    fun set(newvalue: T){
        newvalue?.let {
            if(newvalue.equals(value))return
            value = newvalue
            SharePreferenceMgr.setValue(key, value)
        }
    }
}