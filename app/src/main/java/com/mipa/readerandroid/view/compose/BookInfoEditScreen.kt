package com.mipa.readerandroid.view.compose


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.mipa.readerandroid.R
import com.mipa.readerandroid.base.dialogcontroller.BookInfoEditDialogController
import com.mipa.readerandroid.base.CDMap
import com.mipa.readerandroid.base.combineHost
import com.mipa.readerandroid.view.compose.base.LoadingCompose
import com.mipa.readerandroid.view.composedata.BookInfoEditCD

//todo 对于进行耗时操作或有error的compose，是否可以分为加载界面，错误界面，正常界面，以及上或下的bar这4部分
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BookEditScreen(controller: BookInfoEditDialogController) {

    val viewModel = CDMap.get<BookInfoEditCD>()
    val onCancel:()->Unit = {controller.dismiss()}



    val newTagState = remember { mutableStateOf(TextFieldValue("")) }

    // 协程作用域
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // 保存按钮启用状态
    val isSaveEnabled = remember {
        derivedStateOf {
            viewModel.title.value.isNotEmpty() &&
                    viewModel.description.value.isNotEmpty()
        }
    }

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
        uri?.let { viewModel.updateCoverImg(it, onCancel) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("编辑书籍信息") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            viewModel.onSave(onCancel)
                        },
                        enabled = isSaveEnabled.value
                    ) {
                        Text(
                            "保存",
                            color = if (isSaveEnabled.value) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (viewModel.isSaving.value) {
                LoadingCompose()
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(16.dp)
                ) {
                    // 封面图片和基本信息
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // 封面图片
                        Box(
                            modifier = Modifier
                                .clickable {
                                    launcher.launch("image/*")
                                }
                                .size(120.dp, 180.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            if (viewModel.coverImage.value != null) {
                                val painter = rememberAsyncImagePainter(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(viewModel.coverImage.value.combineHost())
                                        .placeholder(R.drawable.default_avatar)
                                        .error(R.drawable.default_avatar)
                                        .crossfade(true)
                                        .build())
                                // 实际项目中可以使用Coil等库加载网络图片
                                Image(
                                    painter = painter, // 占位图
                                    contentDescription = "书籍封面",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                // 没有封面时的占位符
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        Icons.Default.Add,
                                        contentDescription = "无封面",
                                        modifier = Modifier.size(48.dp),
                                        tint = MaterialTheme.colorScheme.secondary
                                    )
                                    Text(
                                        text = "添加封面",
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }
                            }
                        }

                        // 基本信息编辑
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // 书名
                            OutlinedTextField(
                                value = viewModel.title.value,
                                onValueChange = { viewModel.title.value = it },
                                label = { Text("书名") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Text
                                )
                            )


                            // 类别
                            OutlinedTextField(
                                value = viewModel.category.value,
                                onValueChange = { viewModel.category.value = it },
                                label = { Text("类别") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Text
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // 描述
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "内容简介",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        OutlinedTextField(
                            value = viewModel.description.value,
                            onValueChange = { viewModel.description.value = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Text
                            ),
                            maxLines = 5,
                            placeholder = { Text("请输入书籍简介...") }
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // 标签管理
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "标签",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        // 标签输入框
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = newTagState.value,
                                onValueChange = { newTagState.value = it },
                                modifier = Modifier.weight(1f),
                                placeholder = { Text("输入标签...") },
                                singleLine = true
                            )
                            Button(
                                onClick = {
                                    val tagText = newTagState.value.text.trim()
                                    if (tagText.isNotEmpty()&&!viewModel.tags.contains(tagText)) {
                                        viewModel.tags.add(tagText)
                                        newTagState.value = TextFieldValue("")
                                    }
                                },
                                modifier = Modifier.align(Alignment.Bottom)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "添加标签")
                            }
                        }

                        // 标签列表
                        if (viewModel.tags.isNotEmpty()) {
                            FlowRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                viewModel.tags.forEachIndexed { index, tag ->
                                    AssistChip(
                                        label = { Text(tag) },
                                        trailingIcon = {
                                            IconButton(
                                                onClick = { viewModel.tags.removeAt(index) },
                                                modifier = Modifier.size(16.dp)
                                            ) {
                                                Icon(Icons.Default.Close, contentDescription = "移除标签", Modifier.size(14.dp))
                                            }
                                        },
                                        modifier = Modifier,
                                        onClick = {}
                                    )
                                }
                            }
                        }
                    }

                    // 底部间距
                    Spacer(modifier = Modifier.height(48.dp))
                }
            }

        }
    }
}





