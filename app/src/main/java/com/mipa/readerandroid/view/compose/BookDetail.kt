package com.mipa.readerandroid.view.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.base.ConstValue
import com.mipa.readerandroid.model.feature.Book
import com.mipa.readerandroid.model.feature.Chapter
import com.mipa.readerandroid.view.compose.base.IconAndTextItem
import com.mipa.readerandroid.view.compose.base.LoadingCompose
import com.mipa.readerandroid.view.composedata.BookDetailCD
import com.mipa.readerandroid.view.composedata.ChapterListPageCD
import kotlinx.coroutines.Job
import kotlinx.coroutines.awaitCancellation

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen( ) {
    var isDescriptionExpanded by remember { mutableStateOf(false) }

    val viewModel = CDMap.get<BookDetailCD>()

    val book = viewModel.book.value
    val isLoading by viewModel.isLoading.collectAsState()


    val naviController = LocalNavController.current

    LaunchedEffect(book) {
        CDMap.get<ChapterListPageCD>().setBook(book)
    }

    DisposableEffect(Unit) {
        viewModel.loadBook()
        onDispose {
            viewModel.cancelLoad()
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()){
        if (isLoading) {
            LoadingCompose()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                BookInfoSection(book, isDescriptionExpanded) { isDescriptionExpanded = !isDescriptionExpanded }
                ActionButtonsSection()
                IconAndTextItem(Icons.Default.ChairAlt, " 目录", onClick = {
                    viewModel.onClickDirItem(naviController)
                })
                ChapterListScreen()
                Spacer(modifier = Modifier.height(80.dp))
            }




            // 底部悬浮按钮
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Button(
                    onClick = { /* 开始阅读 */ },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("开始阅读", fontSize = 18.sp)
                }
            }
        }
    }
}

@Composable
fun BookInfoSection(book: Book, isDescriptionExpanded: Boolean, onDescriptionClick: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        // 书籍封面和基本信息
        Row(modifier = Modifier.fillMaxWidth()) {
            // 书籍封面
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(180.dp)
                    .shadow(8.dp, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
            ) {
                // 实际项目中应该使用网络图片加载库如Coil或Glide
                Image(
                    painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                    contentDescription = book.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // 书籍基本信息
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = book.title?:"",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "作者：${book.authorName}",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RatingBar(rating = book.rating?: 0f)
                    Text(
                        text = "${book.rating}",
                        fontSize = 14.sp,
                        color = Color(0xFFFFA000),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                PriceTag(book.price?:0f)
            }
        }

        // 书籍描述
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .clickable(onClick = onDescriptionClick),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "内容简介",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = book.description?:"",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = if (isDescriptionExpanded) Int.MAX_VALUE else 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = if (isDescriptionExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (isDescriptionExpanded) "收起" else "展开",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun RatingBar(rating: Float, maxRating: Int = 5) {
    Row {
        for (i in 1..maxRating) {
            Icon(
                imageVector = if (i <= rating) Icons.Filled.Star else Icons.Filled.StarBorder,
                contentDescription = null,
                tint = Color(0xFFFFA000),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun PriceTag(price: Float) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFFFF5722), Color(0xFFFF9800))
                )
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = "¥${price}",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Composable
fun ActionButtonsSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ActionButton(icon = Icons.Default.Download, text = "下载")
        ActionButton(icon = Icons.Default.Comment, text = "评论")
        ActionButton(icon = Icons.Default.ThumbUp, text = "推荐")
        ActionButton(icon = Icons.Default.Share, text = "分享")
    }
}

@Composable
fun ActionButton(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { /* 点击操作 */ }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}



