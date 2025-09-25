package com.mipa.readerandroid.view.composedata

import androidx.navigation.NavHostController
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.model.feature.Book
import com.mipa.readerandroid.service.SearchService

class SearchPageCD: DatasShowViewModel<Book>() {

    var keyword: String = ""
    val searchSuggestions = listOf(
        "玄幻",
        "都市",
        "穿越",
        "言情",
        "修真",
        "历史",
        "游戏",
        "科幻",
        "恐怖",
        "悬疑"
    )

    override suspend fun getMoreData(pageNumber: Int, pageSize: Int): List<Book> {
        return SearchService.searchByKeyword(keyword, pageNumber, pageSize)
    }

    override fun onItemClick(data: Book, naviController: NavHostController) {
        data.bookId?.let { CDMap.get<BookDetailCD>().from(data) }
        naviController.navigate(ConstValue.ROUTER_BOOK_DETAIL){
            launchSingleTop = true
        }
    }

    fun onSearch(keyword: String){
        if(this.keyword != keyword){
            this.keyword = keyword
            refresh()
        }
    }

}