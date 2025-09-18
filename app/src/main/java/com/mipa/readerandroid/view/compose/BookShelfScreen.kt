package com.mipa.readerandroid.view.compose


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.mipa.readerandroid.R
import com.mipa.readerandroid.model.feature.Book
import com.mipa.readerandroid.view.compose.base.LoadingCompose
import com.mipa.readerandroid.view.composedata.BookShelfPageCD
import com.mipa.readerandroid.view.composedata.MePageCD

/**
 * 书架界面组件
 * @param books 书籍列表数据
 * @param onBookClick 点击书籍项的回调函数
 * @param contentPadding 内容内边距（可选）
 */
@Composable
fun BookshelfScreen() {

    val viewModel = BookShelfPageCD
    val librarys = viewModel.datas
    val isLoading by viewModel.isLoading.collectAsState()
    val hasMoreData by viewModel.hasMoreData.collectAsState()

    val naviController = LocalNavController.current

    if(!MePageCD.isLogin()){
        Box(modifier = Modifier.fillMaxSize()){
            Text("登录后才能使用书架", modifier = Modifier.align(Alignment.Center), fontSize = 24.sp)
        }
        return
    }

    LaunchedEffect(viewModel.flushFlag.value) {
        viewModel.refresh()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        // 使用LazyVerticalGrid实现3列布局
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxSize().padding(8.dp)
        ) {

            items(librarys) { library ->
                library.book?.let {
                    BookItemInBookshelf(
                        book = it,
                        onClick = { viewModel.onBookClick(library, naviController) },
                        onDelete = { viewModel.onDeleteClick(library)}
                    )
                }
            }
            // 加载指示器
            if (isLoading) {
                item {
                    LoadingCompose()
                }
            }

            // 没有更多数据时显示
            if (!hasMoreData) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "已经到底啦~")
                    }
                }
            }

        }
    }
}

/**
 * 书架中的单个书籍项组件
 * @param book 书籍数据
 * @param onClick 点击回调函数
 */
@Composable
fun BookItemInBookshelf(
    book: Book,
    onClick: () -> Unit,
    onViewDetails: (Book) -> Unit = { onClick() },
    onEdit: (Book) -> Unit = {},
    onDelete: (Book) -> Unit = {}
) {
    // 跟踪是否显示操作蒙层
    var showOverlay by remember { mutableStateOf(false) }

    // 处理长按事件
    val longPressModifier = Modifier.pointerInput(Unit) {
        detectTapGestures(
            onTap = { onClick() },
            onLongPress = { showOverlay = true }
        )
    }

    Column(
        modifier = longPressModifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 书籍封面（带阴影效果）
        Card(
            modifier = Modifier
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(8.dp)
                )
                .aspectRatio(0.7f), // 设置封面宽高比
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile_avatar),
                contentDescription = book.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // 书籍标题（最多显示2行）
        Text(
            text = book.title.orEmpty(),
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        // 长按操作蒙层
        if (showOverlay) {
            Popup(
                onDismissRequest = { showOverlay = false },
                properties = PopupProperties(focusable = true)
            ) {
                // 点击外部区域关闭蒙层
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = { showOverlay = false })
                        }
                ) {
                    // 蒙层内容
                    Surface(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                            .shadow(24.dp, RectangleShape)
                            .fillMaxWidth(0.8f), // 蒙层宽度
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "操作选项",
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            // 操作按钮行
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                // 查看详情
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.clickable {
                                        showOverlay = false
                                        onViewDetails(book)
                                    }
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(50),
                                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                        modifier = Modifier.padding(12.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.profile_avatar),
                                            contentDescription = "查看详情",
                                            modifier = Modifier.size(40.dp),
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    Text(
                                        text = "详情",
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }

                                // 编辑
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.clickable {
                                        showOverlay = false
                                        onEdit(book)
                                    }
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(50),
                                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                                        modifier = Modifier.padding(12.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.profile_avatar),
                                            contentDescription = "编辑",
                                            modifier = Modifier.size(40.dp),
                                            tint = MaterialTheme.colorScheme.secondary
                                        )
                                    }
                                    Text(
                                        text = "编辑",
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }

                                // 删除
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.clickable {
                                        showOverlay = false
                                        onDelete(book)
                                    }
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(50),
                                        color = Color.Red.copy(alpha = 0.1f),
                                        modifier = Modifier.padding(12.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.profile_avatar),
                                            contentDescription = "删除",
                                            modifier = Modifier.size(40.dp),
                                            tint = Color.Red
                                        )
                                    }
                                    Text(
                                        text = "删除",
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}