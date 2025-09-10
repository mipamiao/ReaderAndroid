package com.mipa.readerandroid.view.compose.base

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mipa.readerandroid.model.feature.Book
import com.mipa.readerandroid.R


@Composable
fun BookItem(
    book: Book,
    onBookClick: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onBookClick(book) }
            .padding(8.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            // 书籍封面
            Box(modifier = Modifier.size(96.dp, 140.dp)) {
                if (book.coverUrl != null) {
                    // 实际项目中可以使用Coil等库加载网络图片
                    Image(
                        painter = painterResource(R.drawable.profile_avatar), // 占位图
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
                            text = book.title.take(1),
                            fontSize = 48.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }

            // 书籍信息
            Column(modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)) {
                Text(
                    text = book.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "作者：${book.author}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = book.description,
                    fontSize = 14.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "￥${book.price}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}