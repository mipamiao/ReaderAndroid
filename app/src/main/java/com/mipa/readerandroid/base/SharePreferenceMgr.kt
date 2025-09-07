package com.mipa.readerandroid.base

import android.content.Context
import android.content.SharedPreferences

object SharePreferenceMgr {
    val defaultSP by lazy {
        MyApp.getInstance().getContext().getSharedPreferences("reader", Context.MODE_PRIVATE)
    }
    @Suppress("UNCHECKED_CAST")
    fun <T> getValue(key: String, defaultValue: T): T {
        return when (defaultValue) {
            is Int -> defaultSP.getInt(key, defaultValue) as T
            is String -> defaultSP.getString(key, defaultValue) as T
            is Boolean -> defaultSP.getBoolean(key, defaultValue) as T
            is Float -> defaultSP.getFloat(key, defaultValue) as T
            is Long -> defaultSP.getLong(key, defaultValue) as T
            else -> throw IllegalArgumentException("Unsupported type: ${defaultValue.toString()}")
        }
    }

    fun <T> setValue(key: String, value: T) {
        val editor = defaultSP.edit()
        when (value) {
            is Int -> editor.putInt(key, value)
            is String -> editor.putString(key, value)
            is Boolean -> editor.putBoolean(key, value)
            is Float -> editor.putFloat(key, value)
            is Long -> editor.putLong(key, value)
            else -> throw IllegalArgumentException("Unsupported type: ${value.toString()}")
        }
        editor.apply()
    }

}
inline fun <reified T> SharedPreferences.get(key: String, defaultValue: T): T {
    return when (T::class) {
        Int::class -> getInt(key, defaultValue as Int) as T
        String::class -> getString(key, defaultValue as String) as T
        Boolean::class -> getBoolean(key, defaultValue as Boolean) as T
        Float::class -> getFloat(key, defaultValue as Float) as T
        Long::class -> getLong(key, defaultValue as Long) as T
        else -> throw IllegalArgumentException("Unsupported type: ${T::class.java.name}")
    }
}