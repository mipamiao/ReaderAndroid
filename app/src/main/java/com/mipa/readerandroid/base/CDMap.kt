package com.mipa.readerandroid.base

import kotlin.reflect.full.createInstance // 明确导入

object CDMap {
    val map = mutableMapOf<String, BaseCD>()

    inline fun <reified T : BaseCD> put(value: T): T {
        val key = T::class.java.simpleName
        map.put(key, value)
        return value
    }

    inline fun <reified T : BaseCD> get(): T {
        val key = T::class.java.simpleName
        if (map.containsKey(key)) {
            return map[key] as T
        } else {
            val instance = T::class.createInstance()
            map[key] = instance
            return instance
        }
    }

    inline fun <reified T> del() {
        val key = T::class.java.simpleName
        if (map.containsKey(key)) {
            map.remove(key)
        }
    }

    fun del(cd: BaseCD) {
        val key = cd.javaClass.simpleName
        if (map.containsKey(key)) {
            map.remove(key)
        }
    }

}