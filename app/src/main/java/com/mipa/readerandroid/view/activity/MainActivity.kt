package com.mipa.readerandroid.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.mipa.model.UserProfileViewModel
import com.mipa.readerandroid.base.AbsActivity
import com.mipa.readerandroid.ui.theme.ReaderAndroidTheme
import com.mipa.readerandroid.view.compose.MainNavigation
import com.mipa.readerandroid.view.composedata.MePageCD

class MainActivity :AbsActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReaderAndroidTheme {
                MainNavigation()
            }
        }
        val status = getStatusBarHeight()
        Log.e("TAG", "onCreate: $status", )
    }
}

@Preview(showBackground = true)
@Composable
fun PersonalProfilePreview() {
    ReaderAndroidTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "我的书籍",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
            IconButton(
                onClick = {  },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "修改书籍信息",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

fun Context.getStatusBarHeight(): Int {
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return if (resourceId > 0) {
        resources.getDimensionPixelSize(resourceId)
    } else {
        0
    }
}



