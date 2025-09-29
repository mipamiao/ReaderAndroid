package com.mipa.readerandroid.view.composedata

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.mipa.readerandroid.base.BaseCD
import com.mipa.readerandroid.base.ConstValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class DatasShowAllViewModel<T> : BaseCD() {
    companion object{
        const val TAG = "DatasShowAllViewModel<T>"
    }
    val datas = mutableStateListOf<T>()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var _flushFlag = mutableStateOf(0)
    val flushFlag: State<Int> = _flushFlag

    fun loadAlllDatas() {
        if (_isLoading.value ) {
            return
        }
        Log.e(TAG, "loadAlllDatas: pre", )
        _isLoading.value = true
        viewModelScope.launch {
            val res = withContext(Dispatchers.IO) {
                ConstValue.delay()
                Log.e(TAG, "loadAlllDatas: datas_size = ${datas.size}", )
                getAllData()

            }
            Log.e(TAG, "loadAlllDatas: ${res.size}", )
            datas.clear()
            datas.addAll(res)
            _isLoading.value = false
        }
    }

    fun needFlush(){
        _flushFlag.value += 1
    }

    abstract suspend fun getAllData(): List<T>

    abstract fun onItemClick(data: T, naviController: NavHostController)
}