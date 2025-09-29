package com.mipa.readerandroid.base

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

object CustomedSettingJDS {
    val READER_FONT_SIZE = intPreferencesKey("font_size")
    val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_settings")
}