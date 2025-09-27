package com.mipa.readerandroid.base.EffectController

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class EffectController {
    private val _trigger = MutableStateFlow<Int>(0)
    val trigger: StateFlow<Int> = _trigger

    fun touch() {
        _trigger.value++
    }
}