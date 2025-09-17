package com.mipa.readerandroid.model.feature

import com.google.gson.annotations.SerializedName

data class ChapterInfo(
    var authorId: String? = null,
    var bookId: String? = null,
    var chapterId: String? = null,
    var title: String? = null,
    var order: Int? = null,
    var wordCount: Int = 1000,
    var createdAt: String? = null,
    var updatedAt: String? = null
)
data class Chapter(
    @SerializedName("chapterInfoDTO")
    var chapterInfo:ChapterInfo? = null,
    var content: String?
)