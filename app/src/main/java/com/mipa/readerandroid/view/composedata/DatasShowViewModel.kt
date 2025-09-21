package com.mipa.readerandroid.view.composedata

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.mipa.readerandroid.base.ConstValue
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.navigation.NavHostController
import com.mipa.readerandroid.base.BaseCD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class DatasShowViewModel<T>: BaseCD() {
    val datas = mutableStateListOf<T>()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _hasMoreData = MutableStateFlow(true)
    val hasMoreData: StateFlow<Boolean> = _hasMoreData

    private var _flushFlag = mutableStateOf(0)
    val flushFlag:State<Int> = _flushFlag

    private var currentPage = 0
    private val pageSize = 20


    fun loadMoreDatas() {
        if (_isLoading.value || !_hasMoreData.value) {
            return
        }
        _isLoading.value = true
        viewModelScope.launch {
            val res = withContext(Dispatchers.IO) {
                ConstValue.delay()
                getMoreData(currentPage, pageSize)
            }
            datas.addAll(res)
            currentPage++
            _hasMoreData.value = res.size == pageSize
            _isLoading.value = false
        }
    }

    open fun refresh() {
        datas.clear()
        _isLoading.value = false
        _hasMoreData.value = true
        currentPage = 0
        loadMoreDatas()
    }

    fun needFlush(){
        _flushFlag.value += 1
    }


    abstract suspend fun getMoreData(pageNumber: Int, pageSize: Int): List<T>

    abstract fun onBookClick(data: T, naviController: NavHostController)
}