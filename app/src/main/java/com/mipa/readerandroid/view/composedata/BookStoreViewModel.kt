package com.mipa.readerandroid.view.composedata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mipa.readerandroid.model.feature.Book
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

object BookStoreViewModel : ViewModel() {
    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _hasMoreData = MutableStateFlow(true)
    val hasMoreData: StateFlow<Boolean> = _hasMoreData

    private var currentPage = 1
    private val pageSize = 20

    init {
        // 初始加载数据
        loadMoreBooks()
    }

    fun loadMoreBooks() {
        if (_isLoading.value || !_hasMoreData.value) {
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 模拟网络请求延迟
                delay(1000)

                // 模拟加载书籍数据
                val newBooks = generateMockBooks(currentPage, pageSize)

                // 添加新书到列表
                _books.value = _books.value + newBooks

                // 更新页码
                currentPage++

                // 判断是否还有更多数据（这里模拟总共有5页数据）
                _hasMoreData.value = currentPage <= 5
            } catch (e: Exception) {
                // 处理错误
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 模拟生成书籍数据
    private fun generateMockBooks(page: Int, pageSize: Int): List<Book> {
        val startId = (page - 1) * pageSize + 1
        return (startId until startId + pageSize).map {
            Book(
                id = it,
                title = "热门小说标题 $it",
                author = "作者${(it % 10) + 1}",
                description = "这是一本非常精彩的小说，讲述了一个扣人心弦的故事，读者评价很高。",
                price = (10 + (it % 50)).toDouble(),
                rating = (10 + (it % 50)).toFloat()
            )
        }
    }
}