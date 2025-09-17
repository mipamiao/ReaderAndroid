package com.mipa.readerandroid.view.composedata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.model.feature.Book
import com.mipa.readerandroid.model.feature.Chapter
import com.mipa.readerandroid.model.feature.ChapterInfo
import com.mipa.readerandroid.service.ChapterService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

object ChapterListPageCD : ChaptersShowViewModel() {

    override suspend fun getMoreData(): List<ChapterInfo> {
        book.value.bookId?.let {
            return ChapterService.listChapters(it)
        }
        return emptyList()
    }

}