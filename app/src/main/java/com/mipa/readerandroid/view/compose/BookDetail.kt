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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mipa.readerandroid.model.feature.Book
import com.mipa.readerandroid.model.feature.Chapter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(book: Book, chapters: List<Chapter>, onBackClick: () -> Unit, onChapterClick: (Chapter) -> Unit) {
    var isDescriptionExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("书籍详情") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    IconButton(onClick = { /* 分享功能 */ }) {
                        Icon(Icons.Default.Share, contentDescription = "分享")
                    }
                    IconButton(onClick = { /* 收藏功能 */ }) {
                        Icon(Icons.Default.Favorite, contentDescription = "收藏")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 书籍信息区域
            item {
                BookInfoSection(book, isDescriptionExpanded) { isDescriptionExpanded = !isDescriptionExpanded }
            }

            // 操作按钮区域
            item {
                ActionButtonsSection()
            }

            // 章节列表标题
            item {
                ChapterListHeader(chapters.size)
            }

            // 章节列表
            items(chapters) { chapter ->
                ChapterItem(chapter, onChapterClick)
            }

            // 底部空间
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
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
                    text = book.title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "作者：${book.author}",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RatingBar(rating = book.rating)
                    Text(
                        text = "${book.rating}",
                        fontSize = 14.sp,
                        color = Color(0xFFFFA000),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                PriceTag(book.price)
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
                    text = book.description,
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
fun PriceTag(price: Double) {
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

@Composable
fun ChapterListHeader(chapterCount: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "目录 ($chapterCount)",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.SwapVert,
                contentDescription = "排序",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "倒序",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }

    Divider(modifier = Modifier.padding(horizontal = 16.dp))
}

@Composable
fun ChapterItem(chapter: Chapter, onChapterClick: (Chapter) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onChapterClick(chapter) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (chapter.isVip) {
                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(width = 24.dp, height = 16.dp)
                        .background(
                            color = Color(0xFFFF6D00),
                            shape = RoundedCornerShape(2.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "VIP",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = chapter.title,
                fontSize = 16.sp,
                color = if (chapter.isLocked) Color.Gray else MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
                //modifier = Modifier.weight(1f)
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "${chapter.wordCount}字",
                fontSize = 12.sp,
                color = Color.Gray
            )

            if (chapter.isLocked) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "锁定",
                    tint = Color.Gray,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(16.dp)
                )
            }
        }
    }

    Divider(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        thickness = 0.5.dp,
        color = Color.LightGray
    )
}

@Preview(showBackground = true)
@Composable
fun BookDetailScreenPreview() {
    val sampleBook = Book(
        id = 1,
        title = "三体",
        author = "刘慈欣",
        coverUrl = "",
        description = "文化大革命如火如荼进行的同时，军方探寻外星文明的绝秘计划\"红岸工程\"取得了突破性进展。但在按下发射键的那一刻，历经劫难的叶文洁没有意识到，她彻底改变了人类的命运。地球文明向宇宙发出的第一声啼鸣，以太阳为中心，以光速向宇宙深处飞驰……\n\n四光年外，\"三体文明\"正苦苦挣扎——三颗无规则运行的太阳主导下的百余次毁灭与重生逼迫他们逃离母星。而恰在此时，他们接收到了地球发来的信息。在运用超技术锁死地球人的基础科学之后，三体人庞大的宇宙舰队开始向地球进发……\n\n人类的末日悄然来临。",
        price = 39.8,
        rating = 4.9f
    )

    val sampleChapters = listOf(
        Chapter(id = "1", title = "第一章 疯狂年代", wordCount = 3200),
        Chapter(id = "2", title = "第二章 射手和农场主", wordCount = 2800),
        Chapter(id = "3", title = "第三章 红岸之一", wordCount = 3500),
        Chapter(id = "4", title = "第四章 红岸之二", wordCount = 3100),
        Chapter(id = "5", title = "第五章 叶文洁", wordCount = 4200, isVip = true),
        Chapter(id = "6", title = "第六章 宇宙闪烁之一", wordCount = 3800, isVip = true, isLocked = true),
        Chapter(id = "7", title = "第七章 宇宙闪烁之二", wordCount = 4100, isVip = true, isLocked = true),
        Chapter(id = "8", title = "第八章 聚会", wordCount = 3600, isVip = true, isLocked = true)
    )

    MaterialTheme {
        BookDetailScreen(
            book = sampleBook,
            chapters = sampleChapters,
            onBackClick = {},
            onChapterClick = {}
        )
    }
}