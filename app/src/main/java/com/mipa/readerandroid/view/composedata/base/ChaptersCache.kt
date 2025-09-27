package com.mipa.readerandroid.view.composedata.base

import android.util.Log
import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.model.feature.Chapter

class ChaptersCache {

    var bookId: String? = null
        set(value) {
            for(item in chapterList){
                item.bookId = value
            }
            value
        }

    var orderNum: Int? = null

    var leftCacheNumber = 2
    var rightCacheNumber = 2

    var chapterList = mutableListOf<ChapterCache>()

    init {
        for (i in 1..leftCacheNumber+rightCacheNumber+1)
            chapterList.add(ChapterCache())
    }

    //todo 可以有性能上的优化
    fun get(
        order: Int,
        onStart: () -> Unit = {},
        onEnd: (Chapter?) -> Unit = {}
    ): ChapterCache {
        if(!checkValue())return ChapterCache()
        val newStartOrder = order - leftCacheNumber
        val newEndOrder = order + rightCacheNumber
        val tempList = mutableListOf<ChapterCache>()
        for (orderIndex in newStartOrder..newEndOrder) {
            chapterList.find { predicate ->
                predicate.nowOrder == orderIndex
            }?.let {
                chapterList.remove(it)
                tempList.add(it)
            }
        }
        tempList.addAll(chapterList)
        chapterList = tempList
        for (index in 0..(leftCacheNumber + rightCacheNumber)) {
            if (index + newStartOrder>=0&&index + newStartOrder < orderNum!!) {
                chapterList[index].targetOrder = index + newStartOrder
            }
        }

        chapterList.map { chapter->
            chapter.visible = chapter.targetOrder == order
            chapter.flushChapterByOrder(onStart = onStart, onEnd = onEnd)
        }
        logList()

        return chapterList[leftCacheNumber]
    } //todo 快速滑动，前一组还没加载完就下一组


    private fun checkValue():Boolean{
        if(orderNum==null){
            ConstValue.showToast("请给ChaptersCache设置章节数")
            return false
        }
        return true
    }

    private fun logList(){
        var str = "[ "
        for(item in chapterList){
            val res = "[ ${item.nowOrder} --> ${item.targetOrder}, ${item.visible} ]"
            str += res + ", "
        }
        str += ']'
        Log.e("logList", "logList: $str", )
    }
}