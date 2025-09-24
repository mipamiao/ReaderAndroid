package com.mipa.readerandroid.view.compose.dialog

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mipa.readerandroid.base.BookInfoEditDialogController
import com.mipa.readerandroid.view.compose.BookEditScreen
import com.mipa.readerandroid.view.compose.base.LoadingCompose

@Composable
fun BookInfoEditDialog(bookInfoEditDialogController: BookInfoEditDialogController) {
    if(bookInfoEditDialogController.isShow) {
        Dialog(
            onDismissRequest = { bookInfoEditDialogController.onDismiss() }
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f),
                shape = RoundedCornerShape(16.dp)
            ){
                BookEditScreen(bookInfoEditDialogController)
            }
        }
    }
}