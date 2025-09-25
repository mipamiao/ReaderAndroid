package com.mipa.readerandroid.view.compose


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mipa.readerandroid.R
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.base.combineHost
import com.mipa.readerandroid.model.feature.Book
import com.mipa.readerandroid.view.compose.base.LoadingCompose
import com.mipa.readerandroid.view.compose.dialog.BookInfoEditDialog
import com.mipa.readerandroid.view.composedata.MyBookPageCD

@Composable
fun MyBooksScreen() {
    val viewModel: MyBookPageCD = CDMap.get<MyBookPageCD>()
    val myBooks = viewModel.datas
    val isLoading by viewModel.isLoading.collectAsState()
    val hasMoreData by viewModel.hasMoreData.collectAsState()


    val naviController = LocalNavController.current

    val listState = rememberLazyListState()

    val isAtBottom = remember {
        derivedStateOf {
            val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
            val totalItemsCount = listState.layoutInfo.totalItemsCount
            lastVisibleItemIndex != null && lastVisibleItemIndex == totalItemsCount - 1
        }
    }

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    if (isAtBottom.value && !isLoading && hasMoreData) {
        LaunchedEffect(true) {
            viewModel.loadMoreDatas()
        }
    }

    BookInfoEditDialog(viewModel.bookInfoEditDialogController)

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = "我的书籍",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
                IconButton(
                    onClick = { viewModel.onBookAddClick() },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        Icons.Default.AddCard,
                        contentDescription = "添加书籍",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }


            if (myBooks.isEmpty() && !isLoading) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Bookmark,
                        contentDescription = "无书籍",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = "您的书架还是空的",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(myBooks.size, key = {myBooks[it].bookId!!}) {
                        MyBookItem(
                            book = myBooks[it],
                            onBookClick={book->
                                viewModel.onItemClick(book, naviController)
                            },
                            onEditClick = { book ->
                                viewModel.onBookEditClick(book, naviController)
                                },
                            onRemoveClick = {book ->
                                viewModel.onBookRemoveClick(book)
                            }
                        )
                    }

                    // 加载指示器
                    if (isLoading) {
                        item {
                            LoadingCompose()
                        }
                    }

                    // 没有更多数据时显示
                    if (!hasMoreData && myBooks.isNotEmpty()) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(text = "已经到底啦~", color = MaterialTheme.colorScheme.secondary)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MyBookItem(
    book: Book,
    onBookClick:(Book) -> Unit,
    onEditClick: (Book) -> Unit,
    onRemoveClick: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onBookClick(book) }
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            // 书籍封面
            Box(modifier = Modifier.size(80.dp, 120.dp)) {
                if (book.coverImage != null) {
                    // 实际项目中可以使用Coil等库加载网络图片
                    val painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(book.coverImage?.combineHost())
                            .placeholder(R.drawable.default_avatar)
                            .error(R.drawable.default_avatar)
                            .crossfade(true)
                            .build())
                    Image(
                        painter = painter, // 占位图
                        contentDescription = "${book.title}封面",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // 没有封面时显示默认封面
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant)) {
                        Text(
                            text = (book.title?:"Default").take(1),
                            fontSize = 40.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }

            // 书籍信息和操作按钮
            Column(modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
                .align(Alignment.CenterVertically)) {
                Text(
                    text = book.title?:"",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "作者：${book.authorName}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "￥${book.price}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // 修改图标按钮
            IconButton(
                onClick = { onEditClick(book) },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "修改书籍信息",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }

            IconButton(
                onClick = { onRemoveClick(book) },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "修改书籍信息",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

//@Composable
//fun BookInfoEditDialog(viewModel: MyBookPageCD) {
//    if (viewModel.showEditDialog.value) {
//        Dialog(
//            onDismissRequest = { viewModel.showEditDialog.value = false }
//        ) {
//            Surface(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .fillMaxHeight(0.9f),
//                shape = RoundedCornerShape(16.dp)
//            ) {
//                viewModel.currentBook?.let {
//                    BookEditScreen(
//                        book = it,
//                        onCancel = { viewModel.showEditDialog.value = false }
//                    )
//                }
//            }
//        }
//    }
//}