package com.mipa.readerandroid.service

import com.mipa.readerandroid.base.PersistValue

object CustomedSettingService {
    val readerFontSize: PersistValue<Int> = PersistValue(18, "readerFontSize")
}