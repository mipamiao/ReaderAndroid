package com.mipa.readerandroid.view.compose.writer

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.mipa.readerandroid.base.BaseCD
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.model.dto.ChapterDto
import com.mipa.readerandroid.model.feature.Chapter
import com.mipa.readerandroid.model.feature.ChapterInfo
import com.mipa.readerandroid.service.ChapterService
import com.mipa.readerandroid.view.composedata.MePageCD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WriterViewCD: BaseCD() {
    var bookId: String? = null
    var chapterId: String? = null
    var order: Int = 0

    val title  = mutableStateOf("")
    val content = mutableStateOf("")


    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun getData() {
        if (isLoading.value) return
        _isLoading.value = true
        viewModelScope.launch {
            val res = withContext(Dispatchers.IO) {
                ConstValue.delay()
                bookId?.let { bookId ->
                    chapterId?.let { chapterId ->
                        ChapterService.getChapter(bookId, chapterId)
                    }
                }
            }
            res?.let {
                title.value = it.chapterInfo?.title?:""
                content.value = it.content?:""
                order = it.chapterInfo?.order?:0
            }?:{
                title.value = ""
                content.value = ""
            }
            _isLoading.value = false
        }
    }

    fun onSave(){
        if (isLoading.value) return
        _isLoading.value = true

        val dto = reWrite()

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                ConstValue.delay()
                chapterId?.let { chapterId ->
                    ChapterService.updateChapter(dto, chapterId)
                } ?: run {
                    ChapterService.addChapter(dto)
                }
            }
            _isLoading.value = false
        }
    }

    private fun reWrite(): ChapterDto {

        val dto = ChapterDto()

        dto.bookId = bookId
        dto.authorId = CDMap.get<MePageCD>().userProfile.value.userId
        dto.title = title.value
        dto.content = content.value
        dto.order = order

        return dto

    }

    fun from(chapterInfo: ChapterInfo?) {
        bookId = chapterInfo?.bookId
        chapterId = chapterInfo?.chapterId
        order = chapterInfo?.order ?: 0
    }

    fun from(bookId: String?, chapterId: String? = null, order: Int = 0) {
        this.bookId = bookId
        this.chapterId = chapterId
        this.order = order
    }
}