package com.mipa.readerandroid.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mipa.model.UserProfileViewModel
import com.mipa.readerandroid.base.AbsActivity
import com.mipa.readerandroid.ui.theme.ReaderAndroidTheme
import com.mipa.readerandroid.view.compose.MainNavigation
import com.mipa.readerandroid.view.composedata.MePageCD

class MainActivity :AbsActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReaderAndroidTheme {
                MainNavigation()
            }
        }
        MePageCD.mainActivity = this

    }
}

@Preview(showBackground = true)
@Composable
fun PersonalProfilePreview() {
    ReaderAndroidTheme {
        MainNavigation()
    }
}



