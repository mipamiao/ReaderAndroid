package com.mipa.readerandroid.view.composedata.base

import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.model.feature.Chapter
import com.mipa.readerandroid.service.ChapterService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChapterCache {

    private val _isLoading = MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> = _isLoading

    var bookId: String? = null

    var chapterId: String? = null

    var nowOrder: Int? = null

    var targetOrder: Int = 0

    var visible = false

    var chapter: Chapter? = null

    var scope = CoroutineScope(Dispatchers.Main)
    var job: Job? = null


    fun flushChapterByOrder(
        onStart: () -> Unit = {},
        onEnd: (Chapter?) -> Unit = {}
    ) {
        if (nowOrder == targetOrder) {
            if (visible) onEnd(chapter)
            return
        }
        _isLoading.value = true
        job?.apply { cancel() }
        job = scope.launch {
            if (visible) onStart()
            val result = withContext(Dispatchers.IO) {
                ConstValue.delay()
                bookId?.let { outer ->
                    targetOrder?.let { inner ->
                        ChapterService.getChapterByOrder(outer, inner)
                    }
                }
            }
            result?.let {
                nowOrder = targetOrder
            }
            chapter = result
            if (visible) onEnd(chapter)
            _isLoading.value = false
        }
    }

    fun clear(){
        job?.cancel()
        scope.cancel()
    }



}
